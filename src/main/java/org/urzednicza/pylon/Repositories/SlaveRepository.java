package org.urzednicza.pylon.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.urzednicza.pylon.models.Slave;
import org.urzednicza.pylon.models.SlaveInfo;

import java.util.List;

public interface SlaveRepository extends JpaRepository<SlaveInfo,Long> {
    public List<SlaveInfo> findAll();
    public SlaveInfo findByAddress(String address);
}
