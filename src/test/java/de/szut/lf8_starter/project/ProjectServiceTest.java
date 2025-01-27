package de.szut.lf8_starter.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository repository;

    @InjectMocks
    private ProjectService service;

    @Test
    void testFilterByManager() {
        // Arrange
        Long managerId = 1L;
        ProjectEntity project = new ProjectEntity();
        project.setCreatedAt(LocalDateTime.now());
        List<ProjectEntity> projects = Arrays.asList(project);
        Page<ProjectEntity> expectedPage = new PageImpl<>(projects);
        
        when(repository.findAllWithFilters(eq(managerId), eq(null), any(Pageable.class)))
            .thenReturn(expectedPage);

        // Act
        Page<ProjectEntity> result = service.readAll(managerId, null, PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testFilterByCustomer() {
        // Arrange
        Long customerId = 1L;
        ProjectEntity project = new ProjectEntity();
        project.setCreatedAt(LocalDateTime.now());
        List<ProjectEntity> projects = Arrays.asList(project);
        Page<ProjectEntity> expectedPage = new PageImpl<>(projects);
        
        when(repository.findAllWithFilters(eq(null), eq(customerId), any(Pageable.class)))
            .thenReturn(expectedPage);

        // Act
        Page<ProjectEntity> result = service.readAll(null, customerId, PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testSortingByCreationDate() {
        // Arrange
        ProjectEntity older = new ProjectEntity();
        older.setCreatedAt(LocalDateTime.now().minusDays(1));
        
        ProjectEntity newer = new ProjectEntity();
        newer.setCreatedAt(LocalDateTime.now());
        
        List<ProjectEntity> projects = Arrays.asList(newer, older);
        Page<ProjectEntity> expectedPage = new PageImpl<>(projects);
        
        when(repository.findAllWithFilters(eq(null), eq(null), any(Pageable.class)))
            .thenReturn(expectedPage);

        // Act
        Page<ProjectEntity> result = service.readAll(null, null, PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getCreatedAt())
            .isAfter(result.getContent().get(1).getCreatedAt());
    }
} 