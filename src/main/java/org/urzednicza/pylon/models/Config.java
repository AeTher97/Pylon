package org.urzednicza.pylon.models;

public class Config {
    private String addr;
    private boolean inspect;


    // Getter Methods

    public String getAddr() {
        return addr;
    }

    public boolean getInspect() {
        return inspect;
    }

    // Setter Methods

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setInspect(boolean inspect) {
        this.inspect = inspect;
    }
}