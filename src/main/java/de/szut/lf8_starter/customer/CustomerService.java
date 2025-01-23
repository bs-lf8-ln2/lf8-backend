package de.szut.lf8_starter.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerEntity create(CustomerEntity entity) {
        return this.repository.save(entity);
    }

    public CustomerEntity getCustomerById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public void deleteCustomerById(Long id) {
        this.repository.deleteById(id);
    }

    public CustomerEntity updateCustomerById(Long id, CustomerEntity entity) {
        if (this.repository.existsById(id)) {
            entity.setId(id);
            return this.repository.save(entity);
        }
        return null;
    }

    public List<CustomerEntity> readAll() {
        return this.repository.findAll();
    }
}
