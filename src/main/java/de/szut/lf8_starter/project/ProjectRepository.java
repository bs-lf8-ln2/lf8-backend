package de.szut.lf8_starter.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByName(String name);


    @Query("SELECT p FROM ProjectEntity p JOIN p.employees e WHERE e.id = :employeeId")
    Collection<ProjectEntity> getProjectEntitiesByEmployees(@Param("employeeId") Long employeeId);

    @Query("SELECT p FROM ProjectEntity p WHERE " +
           "(:managerId IS NULL OR p.projectManager.id = :managerId) AND " +
           "(:customerId IS NULL OR p.customer.id = :customerId) " +
           "ORDER BY p.createdAt DESC")
    Page<ProjectEntity> findAllWithFilters(
        @Param("managerId") Long managerId,
        @Param("customerId") Long customerId,
        Pageable pageable
    );

    default List<ProjectEntity> findAllSorted() {
        return findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    List<ProjectEntity> findByProjectManagerIdAndCustomerId(Long managerId, Long customerId);
    List<ProjectEntity> findByProjectManagerId(Long managerId);
    List<ProjectEntity> findByCustomerId(Long customerId);
}