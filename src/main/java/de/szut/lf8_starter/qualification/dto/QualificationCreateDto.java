package de.szut.lf8_starter.qualification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualificationCreateDto {
    @NotBlank(message = "Skill is required")
    private String skill;

    @JsonCreator
    public QualificationCreateDto(String skill) {
        this.skill = skill;
    }
}
