package org.urzednicza.pylon.models;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SlaveInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String address;
    Long slaveId;
    long ngrokPID;

    public void setNgrokPID(long ngrokPID) {
        this.ngrokPID = ngrokPID;
    }

    public long getNgrokPID() {
        return ngrokPID;
    }

    public void setSlaveId(Long slaveId) {
        this.slaveId = slaveId;
    }

    public Long getSlaveId() {
        return slaveId;
    }

    public Long getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
