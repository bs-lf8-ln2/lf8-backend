package de.szut.lf8_starter.project;

import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectGetIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void testGetProjects_Pagination() throws Exception {
        // Test pagination
        this.mockMvc.perform(get("/projects?page=0&size=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageSize", is(50)))
                .andExpect(jsonPath("$.pageable.pageNumber", is(0)));
    }

    @Test
    @WithMockUser(roles = "user")
    void testGetProjects_Filtering() throws Exception {
        // Test filtering
        this.mockMvc.perform(get("/projects")
                        .param("managerId", "1")
                        .param("customerId", "1"))
                .andExpect(status().isOk());
    }

    // no user role
    @Test
    @WithMockUser
    void testGetProjects_Unauthorized() throws Exception {
        // Test unauthorized access
        this.mockMvc.perform(get("/projects"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "user")
    void testGetProjects_Sorting() throws Exception {
        // Test sorting
        this.mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sort.sorted").value(true));
    }
} 