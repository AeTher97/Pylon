package org.urzednicza.pylon.models;

public class Data {

    private long pid;
    private String address;
    private String payload;

    public void setPid(long pid) {
        this.pid = pid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getAddress() {
        return address;
    }

    public long getPid() {
        return pid;
    }

    public String getPayload() {
        return payload;
    }
}
