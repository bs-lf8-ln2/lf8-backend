package de.szut.lf8_starter.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProjectValidationIT extends AbstractIntegrationTest {

    final String qualificationJson = """
            {
              "skill": "Java"
            }
            """;

    final String qualificationSpringJson = """
            {
              "skill": "Spring"
            }
            """;

    final String customerJson = """
            {
              "name": "ABC Inc."
            }
            """;

    final String customerDEFJson = """
            {
              "name": "DEF Inc."
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

    final String employeeJaneJson = """
            {
              "firstName": "Jane",
              "lastName": "Doe",
              "email": "jane.doe@example.com",
              "department": "IT",
              "qualificationIds": [1]
            }
            """;

    final String projectJson = """
            {
              "name": "New Website Development",
              "projectManager": 1,
              "customer": 1,
              "startDate": "2025-01-15",
              "endDate": "2025-06-30",
              "employeeIds": [1]
            }
            """;


    @Test
    @WithMockUser(roles = "user")
    void testValidation() throws Exception {

        final String wrongProjectJson = """
                {
                  "name": "",
                  "projectManager": 0,
                  "customer": 0,
                  "startDate": "",
                  "endDate": "",
                  "employeeIds": []
                }
                """;


        // Create Project with incorrect values to check validation
        final var projectContentAsString = this.mockMvc.perform(post("/project").content(wrongProjectJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(projectContentAsString).isEmpty();
    }

    @Test
    @WithMockUser(roles = "user")
    void testDuplicateDetection() throws Exception {


        final var qualificationContentAsString = this.mockMvc.perform(post("/qualifications").content(qualificationSpringJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("skill", is("Spring")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var customerContentAsString = this.mockMvc.perform(post("/customer").content(customerDEFJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name", is("DEF Inc.")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var employeeContentAsString = this.mockMvc.perform(post("/employee").content(employeeJaneJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName", is("Jane")))
                .andExpect(jsonPath("lastName", is("Doe")))
                .andExpect(jsonPath("email", is("jane.doe@example.com")))
                .andExpect(jsonPath("department", is("IT")))
                .andExpect(jsonPath("qualifications[0].id", is(1)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final var projectContentAsString = this.mockMvc.perform(post("/projects").content(projectJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name", is("New Website Development")))
                .andExpect(jsonPath("customer.id", is(1)))
                .andExpect(jsonPath("projectManager.id", is(1)))
                .andExpect(jsonPath("startDate", is("2025-01-15")))
                .andExpect(jsonPath("endDate", is("2025-06-30")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // same project
        final var DuplicateProjectContentAsString = this.mockMvc.perform(post("/projects").content(projectJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(DuplicateProjectContentAsString).isNotEmpty();
    }
}
