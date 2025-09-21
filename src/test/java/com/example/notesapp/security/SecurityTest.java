package com.example.notesapp.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testXSSPrevention() throws Exception {
        // Test 1: XSS in title
        String xssPayload = "{\"title\":\"<script>alert('XSS')</script>\",\"content\":\"test\"}";

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(xssPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("&lt;script&gt;alert('XSS')&lt;/script&gt;"));
    }

    @Test
    public void testSQLInjectionPrevention() throws Exception {
        // Test 2: SQL Injection attempt
        String sqlInjectionPayload = "{\"title\":\"'; DROP TABLE notes; --\",\"content\":\"test\"}";

        // Should create note normally (JPA prevents SQL injection)
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sqlInjectionPayload))
                .andExpect(status().isCreated());
    }

    @Test
    public void testClickjackingPrevention() throws Exception {
        // Test 3: Check X-Frame-Options header
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }
}
