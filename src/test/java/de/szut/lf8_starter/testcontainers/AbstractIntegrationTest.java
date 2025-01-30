package de.szut.lf8_starter.testcontainers;

import de.szut.lf8_starter.employee.EmployeeRepository;
import de.szut.lf8_starter.hello.HelloRepository;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.qualification.QualificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * We have to fully reset the tables for each test, deleteAll() does not work because of foreign key constraints.
     */
    @BeforeEach
    void setUp() {
        // Disable foreign key checks temporarily
        jdbcTemplate.execute("SET CONSTRAINTS ALL DEFERRED");

        // Truncate all tables and reset sequences
        jdbcTemplate.execute("TRUNCATE TABLE project_employees CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE project CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE employee_qualifications CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE employee CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE qualification CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE hello CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE customer CASCADE");

        // Reset sequences
        jdbcTemplate.execute("ALTER SEQUENCE project_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE employee_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE qualification_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE customer_id_seq RESTART WITH 1");

        // Re-enable foreign key checks
        jdbcTemplate.execute("SET CONSTRAINTS ALL IMMEDIATE");
    }
}