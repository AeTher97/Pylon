package org.urzednicza.pylon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.urzednicza.pylon.Repositories.SlaveRepository;
import org.urzednicza.pylon.config.Config;
import org.urzednicza.pylon.models.Data;
import org.urzednicza.pylon.models.Task;
import org.urzednicza.pylon.services.TaskService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/computing")
public class PylonController {
    private SlaveRepository slaveRepository;
    private TaskService taskService;

    public PylonController(SlaveRepository slaveRepository, TaskService taskService){
        this.slaveRepository = slaveRepository;
        this.taskService = taskService;
    }

    @GetMapping
    public String welcome(){
        return "Pylon is here";
    }

    @GetMapping("/id")
    public String id()
    { return slaveRepository.findAll().get(0).getId().toString();}

    @PostMapping("/upload")
    public String uploadProgram(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){

        String path = Config.get("programs_path")+ file.getOriginalFilename();
        try {
            Files.copy(file.getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/launch")
    public String startProcess(@RequestParam("name") String name,@RequestParam(value = "param1",required = false) String param1,@RequestParam(value = "param2",required = false) String param2){

        try
        {
            taskService.startNewProcess(name,param1,param2);
            return "Process started successfully";
        }catch (Exception e){
            return "Process failed to start";
        }

    }

    @GetMapping("/stop")
    public String stopProcess( @RequestParam("pid") Long pid){
        try
        {
            taskService.stopProcess( pid);
            return "Process terminated successfully";
        }catch (Exception e){
            return "Could not terminate process";
        }
    }

    @GetMapping("/kill")
    public String killProcess(@RequestParam("pid") Long pid){
        try
        {
            taskService.killProcess( pid);
            return "Process killed successfully";
        }catch (Exception e){
            return "Could not kill process";
        }
    }

    @GetMapping("/restart")
        public String restartProcess(@RequestParam("instance") int instance, @RequestParam("name") String name){
            try {
                taskService.restartProcess(name, instance);
                return "Restarted process successfully";
            }catch (Exception e)
            {
                e.printStackTrace();
                return "Couldn't restart process";
            }

    }

    @GetMapping("/data")
    public String getData(@RequestParam("name") String name, @RequestParam("instance") int instance){
        try{
            StringBuilder payload = new StringBuilder();
            Task task = taskService.getProcess(name,instance);
            Data data = new Data();
            data.setAddress(slaveRepository.findAll().get(0).getAddress());
            data.setPid(task.getPid());
            File file = new File(Config.get("data_path")+task.getName()+task.getInstance()+".txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine())!=null)
                payload.append(line);
            data.setPayload(payload.toString());

            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("/list")
    public String getProcesses(){
        try{
            return  new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(taskService.getProcesses());
        }catch (Exception e){
            return "Failed to obtain list of processes";
        }
    }


}
