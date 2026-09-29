package com.exelynt.resourcebooking.controller;

import com.exelynt.resourcebooking.dto.resources.ResourceResponse;
import com.exelynt.resourcebooking.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResourceService resourceService;

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void userShouldBeAbleToGetResources() throws Exception {

        when(resourceService.getAllResources())
                .thenReturn(List.of(
                        new ResourceResponse(
                                1L,
                                "Conference Room",
                                "Main conference room",
                                "ROOM"
                        )
                ));

        mockMvc.perform(
                        get("/resources")
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void userShouldNotBeAbleToCreateResource() throws Exception {

        mockMvc.perform(
                        post("/resources")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Conference Room",
                                            "description": "Main conference room",
                                            "type": "ROOM"
                                        }
                                        """)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void userShouldNotBeAbleToUpdateResource() throws Exception {

        mockMvc.perform(
                        put("/resources/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Updated Room",
                                            "description": "Updated description",
                                            "type": "ROOM"
                                        }
                                        """)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void userShouldNotBeAbleToDeleteResource() throws Exception {

        mockMvc.perform(
                        delete("/resources/1")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "admin@resourcebooking.com",
            roles = "ADMIN"
    )
    void adminShouldBeAbleToCreateResource() throws Exception {

        when(resourceService.createResource(
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(
                new ResourceResponse(
                        1L,
                        "Conference Room",
                        "Main conference room",
                        "ROOM"
                )
        );

        mockMvc.perform(
                        post("/resources")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Conference Room",
                                            "description": "Main conference room",
                                            "type": "ROOM"
                                        }
                                        """)
                )
                .andExpect(status().isCreated());
    }
}