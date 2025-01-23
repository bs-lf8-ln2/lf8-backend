package de.szut.lf8_starter.employee;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeEntity create(EmployeeEntity entity) {
        return this.repository.save(entity);
    }

    public List<EmployeeEntity> readAll() {
        return this.repository.findAll();
    }

    public EmployeeEntity getEmployeeById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}