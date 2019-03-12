package org.urzednicza.pylon.services;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;


import com.sun.jna.platform.win32.WinNT;

import java.io.BufferedReader;
import java.io.File;

import org.apache.tomcat.jni.Buffer;
import org.apache.tomcat.jni.Proc;
import org.springframework.stereotype.Service;
import org.urzednicza.pylon.Repositories.TaskRepository;
import org.urzednicza.pylon.config.Config;
import org.urzednicza.pylon.models.Task;

import javax.annotation.PreDestroy;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public void startProcess(Task task) throws Exception{
        Runtime runtime = Runtime.getRuntime();
        long processPID = -1;
        File location = new File(Config.get("programs_path"));

        List<Task> tasks = taskRepository.findAllByName(task.getName());
        int number = tasks.size();
        System.out.println(number);
        if(task.getInstance() ==-1)
            task.setInstance(number);

        String dataPath = Config.get("data_path") + task.getName().replace(".exe","") + task.getInstance() + ".txt";

        System.out.println(dataPath);

        ProcessBuilder pb = new ProcessBuilder(Config.get("programs_path") + task.getName(),task.getParam1(),task.getParam2());
        try {
            Process p = pb.start();
            System.out.println("cmd /C cd " + Config.get("programs_path") +" && " + task.getName());
            Field f = p.getClass().getDeclaredField("handle");
            f.setAccessible(true);
            long handl = f.getLong(p);
            Kernel32 kernel = Kernel32.INSTANCE;
            WinNT.HANDLE hand = new WinNT.HANDLE();
            hand.setPointer(Pointer.createConstant(handl));
            processPID = kernel.GetProcessId(hand);

            f.setAccessible(false);

            task.setPid(processPID);
            task.setStatus("Active");


        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void startNewProcess(String programName,String param1,String param2) throws Exception{
        Task task = new Task();
        task.setInstance(-1);
        task.setName(programName);
        task.setParam1(param1);
        task.setParam2(param2);
        startProcess(task);
        taskRepository.save(task);
    }

    public void restartProcess(String programName,int instanceNumber) throws Exception{
        Task task = taskRepository.findByNameAndInstance(programName,instanceNumber);
        startProcess(task);
        taskRepository.save(task);

    }

    public void stopProcess(long pid){
        Runtime runtime = Runtime.getRuntime();
        try {
            Task task = taskRepository.findByPid(pid);
            task.setStatus("Stopped");
            runtime.exec("cmd /C taskkill /PID " + task.getPid()  + " /F ");
            task.setPid(-1);
            taskRepository.save(task);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to terminate process");
        }



    }

    public void killProcess(long pid){
        Runtime runtime = Runtime.getRuntime();
        try {
            Task task = taskRepository.findByPid(pid);
            runtime.exec("cmd /C taskkill /PID " + task.getPid()  + " /F ");
            taskRepository.delete(task);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to kill process");
        }
    }

    public List<Task> getProcesses(){
        List<Task> tasks = taskRepository.findAll();

        for(Task task : tasks){
            if(!checkIfTaskRunning(task.getName(),task.getPid()))
                task.setStatus("Crashed");
                taskRepository.save(task);
        }

        return tasks;
    }

    public Task getProcess(String name,int instance) { return taskRepository.findByNameAndInstance(name,instance);
    }

    private boolean checkIfTaskRunning(String name, Long pid){
        Process p = null;
        try{
            p = Runtime.getRuntime().exec("tasklist");
        }catch (Exception e){
            e.printStackTrace();
        }

        StringBuffer sbInput = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String foundLine = "NONE";

        try{
            while((line = br.readLine())!=null){
                if(line.contains(pid.toString())){
                    foundLine = line;
                }
                sbInput.append(line + "\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            p.waitFor();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        p.destroy();

        try {
            return foundLine.substring(0, foundLine.indexOf(" ")).equals(name);
        }catch (StringIndexOutOfBoundsException e){
            return false;
        }
    }

    @PreDestroy
    public void destroy(){
        for(Task task : taskRepository.findAll()){
            killProcess(task.getPid());
        }
    }
}
