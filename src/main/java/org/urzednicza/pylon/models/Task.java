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
    private String path;
    private String seed;

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

    public void setPath(String path) {
        this.path = path;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getPath() {
        return path;
    }

    public String getSeed() {
        return seed;
    }

    public String getStatus() {
        return Status;
    }
}
