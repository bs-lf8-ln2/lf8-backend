package de.szut.lf8_starter.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByName(String name);
}