package de.szut.lf8_starter.customer;

import de.szut.lf8_starter.customer.dto.CustomerCreateDto;
import de.szut.lf8_starter.customer.dto.CustomerGetDto;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public CustomerGetDto mapToGetDto(CustomerEntity entity) {
        return new CustomerGetDto(entity.getId(), entity.getName());
    }

    public CustomerEntity mapCreateDtoToEntity(CustomerCreateDto dto) {
        return new CustomerEntity(null, dto.getName());
    }
}
