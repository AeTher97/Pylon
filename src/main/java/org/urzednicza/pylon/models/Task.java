package org.urzednicza.pylon.models;

import javax.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long pid;
    private String Status;
    private String name;
    private int instance;
    private String param1;
    private String param2;

    public void setStatus(String status) {
        Status = status;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getPid() {
        return pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }

    public int getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }

    public String getStatus() {
        return Status;
    }
}
