package org.urzednicza.pylon.services;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;


import com.sun.jna.platform.win32.WinNT;
import java.io.File;
import org.apache.tomcat.jni.Proc;
import org.springframework.stereotype.Service;
import org.urzednicza.pylon.Repositories.TaskRepository;
import org.urzednicza.pylon.config.Config;
import org.urzednicza.pylon.models.Task;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public void startProcess(Task task){
        Runtime runtime = Runtime.getRuntime();
        long processPID = -1;
        File location = new File(Config.get("programs_path"));
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
            List<Task> tasks = taskRepository.findAllByName(task.getName());
            int number = tasks.size();
            System.out.println(number);
            if(task.getInstance() ==-1)
                task.setInstance(number);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("cannot start new process");
        }
    }

    public void startNewProcess(String programName,String param1,String param2){
        Task task = new Task();
        task.setInstance(-1);
        task.setName(programName);
        task.setParam1(param1);
        task.setParam2(param2);
        startProcess(task);
        taskRepository.save(task);
    }

    public void restartProcess(String programName,int instanceNumber){
        Task task = taskRepository.findByNameAndInstance(programName,instanceNumber);
        startProcess(task);
        taskRepository.save(task);

    }

    public void terminateProcess(long pid){
        Runtime runtime = Runtime.getRuntime();
        try {
            Task task = taskRepository.findByPid(pid);
            task.setStatus("Terminated");
            task.setPid(-1);
            runtime.exec("cmd /C taskkill /PID " + task.getPid()  + " /F ");
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
            throw new RuntimeException("Failed to terminate process");
        }
    }

    public List<Task> getProcesses(){
        return taskRepository.findAll();
    }
}
