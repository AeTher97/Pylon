package org.urzednicza.pylon.services;

import com.sun.deploy.net.HttpResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.urzednicza.pylon.models.NgrokList;

import java.io.*;

@Service
public class ApplicationService {

    public ApplicationService() {


        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec("cmd /C cd src && cd main && cd resources && ngrok http 3001");
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<NgrokList> response =restTemplate.exchange("http://127.0.0.1:4040/api/tunnels",HttpMethod.GET, request,NgrokList.class);
            System.out.println(response.getBody().getTunnels().get(1).getPublic_url());
            ResponseEntity<NgrokList> response2 =restTemplate.exchange("http://127.0.0.1:4040/api/tunnels",HttpMethod.GET, request,NgrokList.class);


        } catch (IOException e) {
            e.printStackTrace();


        }

    }
}
