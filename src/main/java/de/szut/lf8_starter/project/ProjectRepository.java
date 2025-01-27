package de.szut.lf8_starter.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByName(String name);


    @Query("SELECT p FROM ProjectEntity p JOIN p.employees e WHERE e.id = :employeeId")
    Collection<ProjectEntity> getProjectEntitiesByEmployees(@Param("employeeId") Long employeeId);
}