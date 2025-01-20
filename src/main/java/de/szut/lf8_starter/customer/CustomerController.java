package de.szut.lf8_starter.customer;

import de.szut.lf8_starter.customer.dto.CustomerCreateDto;
import de.szut.lf8_starter.customer.dto.CustomerGetDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController implements CustomerControllerOpenAPI {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping
    public CustomerGetDto create(@RequestBody @Valid CustomerCreateDto customerCreateDto) {
        CustomerEntity customerEntity = this.customerMapper.mapCreateDtoToEntity(customerCreateDto);
        customerEntity = this.customerService.create(customerEntity);
        return this.customerMapper.mapToGetDto(customerEntity);
    }

    @GetMapping("/{id}")
    public CustomerGetDto getById(@PathVariable final Long id) {
        CustomerEntity customerEntity = this.customerService.getCustomerById(id);
        if (customerEntity == null) {
            return null;
        } else {
            return this.customerMapper.mapToGetDto(customerEntity);
        }
    }

    @GetMapping
    public List<CustomerGetDto> findAll() {
        return this.customerService
                .readAll()
                .stream()
                .map(e -> this.customerMapper.mapToGetDto(e))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        this.customerService.deleteCustomerById(id);
    }

    @PutMapping("/{id}")
    public CustomerGetDto updateById(@PathVariable final Long id, @RequestBody @Valid CustomerCreateDto customerCreateDto) {
        CustomerEntity customerEntity = this.customerMapper.mapCreateDtoToEntity(customerCreateDto);
        customerEntity = this.customerService.updateCustomerById(id, customerEntity);
        return this.customerMapper.mapToGetDto(customerEntity);
    }
}
