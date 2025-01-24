package de.szut.lf8_starter.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectMapper projectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "user")
    void testCreateProject_InvalidInput() throws Exception {
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{\n" +
                                "  \"name\": \"\",\n" +  // Invalid input: empty name
                                "  \"projectManager\": 1,\n" +
                                "  \"customer\": 1,\n" +
                                "  \"startDate\": \"2025-01-15\",\n" +
                                "  \"endDate\": \"2025-06-30\",\n" +
                                "  \"employeeIds\": [\n" +
                                "    1\n" +
                                "  ]\n" +
                                "}"))
                .andExpect(status().is4xxClientError());
    }
}