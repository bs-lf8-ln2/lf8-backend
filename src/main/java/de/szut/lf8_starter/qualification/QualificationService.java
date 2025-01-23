package de.szut.lf8_starter.qualification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QualificationService {
    private final QualificationRepository repository;

    public QualificationService(QualificationRepository repository) {
        this.repository = repository;
    }

    public QualificationEntity create(QualificationEntity entity) {
        return this.repository.save(entity);
    }

    public List<QualificationEntity> readAll() {
        return this.repository.findAll();
    }
}