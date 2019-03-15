package org.urzednicza.pylon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
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

    public PylonController(SlaveRepository slaveRepository, TaskService taskService) {
        this.slaveRepository = slaveRepository;
        this.taskService = taskService;
    }


    @GetMapping("/id")
    public String id() {
        return slaveRepository.findAll().get(0).getId().toString();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadProgram(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        new File(Config.get("programs_path")).mkdir();
        String path = Config.get("programs_path") + file.getOriginalFilename();
        try {
            Files.copy(file.getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
            return "Uploaded successfully";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/launch")
    public String startProcess(@RequestParam("name") String name, @RequestParam(value = "seed", required = false) String seed) {

        try {
            taskService.startNewProcess(name, seed);
            return "Process started successfully";
        } catch (Exception e) {
            return "Process failed to start " + e.getMessage();
        }

    }

    @GetMapping("/stop")
    public String stopProcess(@RequestParam("pid") Long pid) {
        try {
            taskService.stopProcess(pid);
            return "Process terminated successfully";
        } catch (Exception e) {
            return "Could not terminate process";
        }
    }

    @GetMapping("/kill")
    public String killProcess(@RequestParam("pid") Long pid) {
        try {
            taskService.killProcess(pid);
            return "Process killed successfully";
        } catch (Exception e) {
            return "Could not kill process";
        }
    }

    @GetMapping("/restart")
    public String restartProcess(@RequestParam("instance") int instance, @RequestParam("name") String name) {
        try {
            taskService.restartProcess(name, instance);
            return "Restarted process successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't restart process";
        }

    }

    @GetMapping("/files")
    public String getFiles() {
        try {
            File folder = new File(Config.get("programs_path"));

            File[] listOfFiles = folder.listFiles();

            StringBuilder list = new StringBuilder();
            try {
                for (File file : listOfFiles) {
                    list.append(file.getName());
                    list.append("\n");
                }
            } catch (Exception e) {
                folder.mkdir();
            }


            return list.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't get files list";
        }
    }

    @GetMapping("/data")
    public String getData(@RequestParam("name") String name, @RequestParam("instance") int instance) {
        try {
            StringBuilder payload = new StringBuilder();
            Task task = taskService.getProcess(name, instance);
            Data data = new Data();
            try {
                data.setAddress(slaveRepository.findAll().get(0).getAddress());
            } catch (Exception e) {
                throw new RuntimeException("Couldn't find running process");
            }
            data.setPid(task.getPid());
            File file = new File(task.getPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null)
                payload.append(line);
            data.setPayload(payload.toString());

            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("/list")
    public String getProcesses() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(taskService.getProcesses());
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to obtain list of processes";
        }
    }


}
