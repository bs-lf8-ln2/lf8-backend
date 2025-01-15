package de.szut.lf8_starter.qualification;

import de.szut.lf8_starter.qualification.dto.QualificationCreateDto;
import de.szut.lf8_starter.qualification.dto.QualificationGetDto;
import org.springframework.stereotype.Service;

@Service
public class QualificationMapper {
    public QualificationGetDto mapToGetDto(QualificationEntity entity) {
        return new QualificationGetDto(entity.getId(), entity.getSkill());
    }

    public QualificationEntity mapCreateDtoToEntity(QualificationCreateDto dto) {
        return new QualificationEntity(dto.getSkill());
    }
}