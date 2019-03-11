package org.urzednicza.pylon.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.urzednicza.pylon.Repositories.SlaveRepository;
import org.urzednicza.pylon.config.Config;
import org.urzednicza.pylon.models.NgrokList;
import org.urzednicza.pylon.models.Slave;
import org.urzednicza.pylon.models.SlaveInfo;
import com.sun.jna.platform.win32.Kernel32;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.ws.Response;
import java.io.*;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@Service
public class ApplicationService {
    private SlaveRepository slaveRepository;

    public ApplicationService(SlaveRepository slaveRepository) {
        //super();
        this.slaveRepository = slaveRepository;

        long processId = -1;
        try {


            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec("cmd /C cd src && cd main && cd resources && ngrok http 3001");
            try {
                TimeUnit.SECONDS.sleep(3);
            }
            catch (Exception e){

                e.printStackTrace();
            }
            try {
                Field f = p.getClass().getDeclaredField("handle");
                ((Field) f).setAccessible(true);
                long handl = f.getLong(p);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE hand = new WinNT.HANDLE();
                hand.setPointer(Pointer.createConstant(handl));
                processId = kernel.GetProcessId(hand);
                f.setAccessible(false);
            }catch (Exception e){
                e.printStackTrace();
            }

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<NgrokList> response = restTemplate.exchange("http://127.0.0.1:4040/api/tunnels", HttpMethod.GET, request, NgrokList.class);

            String address;
            if(response.getBody().getTunnels().get(0).getPublic_url().contains("https"))
                address = response.getBody().getTunnels().get(1).getPublic_url();
            else
                 address = response.getBody().getTunnels().get(0).getPublic_url();

            System.out.println(address);
            System.out.println(processId);


            registerSlave(address,processId);


        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    private void registerSlave(String address,long processId){
        RestTemplate restTemplate = new RestTemplate();
        Slave slave = new Slave();
        slave.setAddress(address);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<String>(new ObjectMapper().writeValueAsString(slave), headers);

            ResponseEntity<Long> response = restTemplate.postForEntity(Config.get("Nexus_url") + "/slave/register",request,Long.class);
            SlaveInfo info = new SlaveInfo();
            info.setAddress(address);
            info.setSlaveId(response.getBody());
            info.setNgrokPID(processId);
            slaveRepository.save(info);
        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreDestroy
    public void destroy(){
        System.out.println("xdvdfg");
        RestTemplate restTemplate = new RestTemplate();
        Slave slave = new Slave();
        slave.setAddress(slaveRepository.findAll().get(0).getAddress());
        slave.setId(slaveRepository.findAll().get(0).getId());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<String>(new ObjectMapper().writeValueAsString(slave), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(Config.get("Nexus_url") + "/slave/unregister",request,String.class);
        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec("cmd /C taskkill /PID" + slaveRepository.findAll().get(0).getNgrokPID() + " /F ");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
