package de.szut.lf8_starter.testcontainers;

import de.szut.lf8_starter.employee.EmployeeRepository;
import de.szut.lf8_starter.hello.HelloRepository;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.qualification.QualificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * A fast slice test will only start jpa context.
 * <p>
 * To use other context beans use org.springframework.context.annotation.@Import annotation.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
@ContextConfiguration(initializers = PostgresContextInitializer.class)
public class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected HelloRepository helloRepository;

    @Autowired
    protected QualificationRepository qualificationRepository;

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        helloRepository.deleteAll();
    }
}
