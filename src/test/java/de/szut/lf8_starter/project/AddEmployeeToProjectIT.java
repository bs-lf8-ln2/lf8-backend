package de.szut.lf8_starter.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class AddEmployeeToProjectIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void testAddEmployeeToProject() throws Exception {
        // Setup: Create qualification, customer, employee, and project
        final String qualificationJson = """
                {
                  "skill": "Java"
                }
                """;

        final String customerJson = """
                {
                  "name": "Test Customer"
                }
                """;

        final String employeeJson = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john.doe@example.com",
                  "department": "IT",
                  "qualificationIds": [1]
                }
                """;

        final String projectJson = """
                {
                  "name": "Test Project",
                  "projectManager": 1,
                  "customer": 1,
                  "startDate": "2025-01-15",
                  "endDate": "2025-06-30",
                  "employeeIds": []
                }
                """;

        // Create qualification
        this.mockMvc.perform(post("/qualifications")
                .content(qualificationJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful());

        // Create customer
        this.mockMvc.perform(post("/customer")
                .content(customerJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful());

        // Create employee
        this.mockMvc.perform(post("/employee")
                .content(employeeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful());

        // Create project
        this.mockMvc.perform(post("/projects")
                .content(projectJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful());

        // Test 1: Add employee to project by name (T_1)
        final String addEmployeeJson = """
                {
                    "employeeId": 1,
                    "qualification": "Java"
                }
                """;

        this.mockMvc.perform(post("/projects/Test Project/employees")
                .content(addEmployeeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees[0].id", is(1)));

        // Test 2: Try to add employee with wrong qualification (T_2)
        this.mockMvc.perform(post("/projects/Test Project/employees")
                .content("""
                        {
                            "employeeId": 1,
                            "qualification": "Python"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(content().string("Employee is already assigned to this project"));

        // Test 3: Try to add same employee to overlapping project (T_3)
        final String overlappingProjectJson = """
                {
                  "name": "Overlapping Project",
                  "projectManager": 1,
                  "customer": 1,
                  "startDate": "2025-03-15",
                  "endDate": "2025-08-30",
                  "employeeIds": []
                }
                """;

        this.mockMvc.perform(post("/projects")
                .content(overlappingProjectJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(post("/projects/Overlapping Project/employees")
                .content(addEmployeeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "user")
    void testAddNonExistentEmployee() throws Exception {
        final String addEmployeeJson = """
                {
                    "employeeId": 999,
                    "qualification": "Java"
                }
                """;

        this.mockMvc.perform(post("/projects/Test Project/employees")
                .content(addEmployeeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "user")
    void testAddEmployeeToNonExistentProject() throws Exception {
        final String addEmployeeJson = """
                {
                    "employeeId": 1,
                    "qualification": "Java"
                }
                """;

        this.mockMvc.perform(post("/projects/NonExistentProject/employees")
                .content(addEmployeeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
} 