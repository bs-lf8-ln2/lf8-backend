package de.szut.lf8_starter.config;

import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.customer.CustomerRepository;
import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.employee.EmployeeRepository;
import de.szut.lf8_starter.hello.HelloEntity;
import de.szut.lf8_starter.hello.HelloRepository;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.qualification.QualificationEntity;
import de.szut.lf8_starter.qualification.QualificationRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@Component
public class SampleDataCreator implements ApplicationRunner {

    private final HelloRepository helloRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final QualificationRepository qualificationRepository;

    public SampleDataCreator(
            HelloRepository helloRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository,
            QualificationRepository qualificationRepository) {
        this.helloRepository = helloRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.qualificationRepository = qualificationRepository;
    }

    public void run(ApplicationArguments args) {
        // Create Hello entities
        helloRepository.save(new HelloEntity("Hallo Welt!"));
        helloRepository.save(new HelloEntity("Schöner Tag heute"));
        helloRepository.save(new HelloEntity("FooBar"));

        // Create Customer
        CustomerEntity customer = new CustomerEntity(null, "ACME Corp");
        customer = customerRepository.save(customer);

        // Create Qualification
        QualificationEntity javaQualification = new QualificationEntity();
        javaQualification.setSkill("Java");
        javaQualification = qualificationRepository.save(javaQualification);

        // Create Employee
        EmployeeEntity employee = new EmployeeEntity();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setQualifications(new HashSet<>());
        employee.getQualifications().add(javaQualification);
        employee = employeeRepository.save(employee);

        // Create Projects
        ProjectEntity projectAlpha = new ProjectEntity();
        projectAlpha.setName("Project Alpha");
        projectAlpha.setCustomer(customer);
        projectAlpha.setProjectManager(employee);
        projectAlpha.setStartDate(LocalDate.of(2024, 1, 15));
        projectAlpha.setEndDate(LocalDate.of(2024, 6, 30));
        projectAlpha.setCreatedAt(LocalDateTime.now());
        projectAlpha.setEmployees(new HashSet<>());
        projectRepository.save(projectAlpha);

        ProjectEntity projectBeta = new ProjectEntity();
        projectBeta.setName("Project Beta");
        projectBeta.setCustomer(customer);
        projectBeta.setProjectManager(employee);
        projectBeta.setStartDate(LocalDate.of(2024, 2, 15));
        projectBeta.setEndDate(LocalDate.of(2024, 7, 30));
        projectBeta.setCreatedAt(LocalDateTime.now());
        projectBeta.setEmployees(new HashSet<>());
        projectRepository.save(projectBeta);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
