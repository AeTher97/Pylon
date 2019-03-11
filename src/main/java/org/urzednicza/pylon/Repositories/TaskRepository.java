package org.urzednicza.pylon.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.urzednicza.pylon.models.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    public Task findByName(String name);
    public Task findByNameAndInstance(String Name,int instance);
    public List<Task> findAllByName(String name);
    public Task findByPid(Long id);
}
