package de.szut.lf8_starter.config;

import de.szut.lf8_starter.hello.HelloEntity;
import de.szut.lf8_starter.hello.HelloRepository;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.employee.EmployeeRepository;
import de.szut.lf8_starter.qualification.QualificationEntity;
import de.szut.lf8_starter.qualification.QualificationRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class SampleDataCreator implements ApplicationRunner {

    private final HelloRepository helloRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final QualificationRepository qualificationRepository;

    public SampleDataCreator(HelloRepository helloRepository, 
                           ProjectRepository projectRepository,
                           EmployeeRepository employeeRepository,
                           QualificationRepository qualificationRepository) {
        this.helloRepository = helloRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.qualificationRepository = qualificationRepository;
    }

    public void run(ApplicationArguments args) {
        // Create sample hello data
        helloRepository.save(new HelloEntity("Hallo Welt!"));
        helloRepository.save(new HelloEntity("Schöner Tag heute"));
        helloRepository.save(new HelloEntity("FooBar"));

        // Create qualification if it doesn't exist
        QualificationEntity javaQualification = new QualificationEntity("Java");
        javaQualification = qualificationRepository.save(javaQualification);

        // Create employee if it doesn't exist
        if (employeeRepository.count() == 0) {
            EmployeeEntity employee = new EmployeeEntity();
            employee.setFirstName("John");
            employee.setLastName("Doe");
            employee.setEmail("john.doe@example.com");
            employee.setDepartment("IT");
            Set<QualificationEntity> qualifications = new HashSet<>();
            qualifications.add(javaQualification);
            employee.setQualifications(qualifications);
            employeeRepository.save(employee);
        }

        // Create sample project if it doesn't exist
        if (projectRepository.count() == 0) {
            ProjectEntity sampleProject = new ProjectEntity();
            sampleProject.setName("Website Development Project");
            sampleProject.setStartDate(LocalDate.of(2025, 1, 15));
            sampleProject.setEndDate(LocalDate.of(2025, 6, 30));
            sampleProject.setEmployees(new HashSet<>(employeeRepository.findAll()));
            projectRepository.save(sampleProject);
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
