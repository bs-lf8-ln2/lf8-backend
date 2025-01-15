package de.szut.lf8_starter.qualification;

import de.szut.lf8_starter.qualification.dto.QualificationCreateDto;
import de.szut.lf8_starter.qualification.dto.QualificationGetDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/qualifications")
public class QualificationController implements QualificationControllerOpenAPI {
    private final QualificationService service;
    private final QualificationMapper qualificationMapper;

    public QualificationController(QualificationService service, QualificationMapper qualificationMapper) {
        this.service = service;
        this.qualificationMapper = qualificationMapper;
    }

    @PostMapping
    public QualificationGetDto create(@RequestBody @Valid QualificationCreateDto qualificationCreateDto) {
        QualificationEntity qualificationEntity = this.qualificationMapper.mapCreateDtoToEntity(qualificationCreateDto);
        qualificationEntity = this.service.create(qualificationEntity);
        return this.qualificationMapper.mapToGetDto(qualificationEntity);
    }

    @GetMapping
    public List<QualificationGetDto> findAll() {
        return this.service
                .readAll()
                .stream()
                .map(e -> this.qualificationMapper.mapToGetDto(e))
                .collect(Collectors.toList());
    }
}