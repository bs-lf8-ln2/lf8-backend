package de.szut.lf8_starter.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomerGetDto {
    private Long id;
    private String name;
}
