package de.szut.lf8_starter.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectPostIT extends AbstractIntegrationTest {

    @Test
    void authorization() throws Exception {

        final String projectJson = """
                {
                  "id": 1,
                  "name": "New Website Development",
                  "startDate": "2025-01-15",
                  "endDate": "2025-06-30",
                  "employees": [
                    {
                      "id": 1,
                      "firstName": "John",
                      "lastName": "Doe",
                      "email": "john.doe@example.com",
                      "department": "IT",
                      "qualifications": [
                        {
                          "id": 1,
                          "skill": "Java"
                        }
                      ]
                    }
                  ]
                }
                """;

        this.mockMvc.perform(post("/projects/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void storeAndFind() throws Exception {
        final String qualificationJson = """
                {
                  "skill": "Java"
                }
                """;

        final String exmployeeJson = """
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
                  "name": "New Website Development",
                  "startDate": "2025-01-15",
                  "endDate": "2025-06-30",
                  "employeeIds": [1]
                }
                """;

        final var contentAsString = this.mockMvc.perform(post("/qualifications").content(qualificationJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("skill", is("Java")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());

        final var employeeContentAsString = this.mockMvc.perform(post("/employee").content(exmployeeJson).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName", is("John")))
                .andExpect(jsonPath("lastName", is("Doe")))
                .andExpect(jsonPath("email", is("john.doe@example.com")))
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
                .andExpect(jsonPath("startDate", is("2025-01-15")))
                .andExpect(jsonPath("endDate", is("2025-06-30")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var projectId = Long.parseLong(new JSONObject(projectContentAsString).get("id").toString());

        final var loadedEntity = projectRepository.findById(projectId);

        assertThat(loadedEntity).isPresent();
        assertThat(loadedEntity.get().getId()).isEqualTo(projectId);
        assertThat(loadedEntity.get().getName()).isEqualTo("New Website Development");
        assertThat(loadedEntity.get().getStartDate()).isEqualTo("2025-01-15");
        assertThat(loadedEntity.get().getEndDate()).isEqualTo("2025-06-30");
    }
}