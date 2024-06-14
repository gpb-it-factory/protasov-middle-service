package ru.gazprombank.middle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gazprombank.middle.dto.UserRegistrationRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenRegistrationIsSuccessful_thenReturnOk() throws Exception {
        var userRegistrationRequest = new UserRegistrationRequest(1L, "icon");
        performPostRequest(userRegistrationRequest, HttpStatus.OK);
    }

    @Test
    public void whenUsernameIsMissing_thenBadRequest() throws Exception {
        var userRegistrationRequest = new UserRegistrationRequest(10L, null);
        performPostRequest(userRegistrationRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenDuplicateRegistration_thenBadRequest() throws Exception {
        var userRegistrationRequest = new UserRegistrationRequest(100L, "wolf");
        performPostRequest(userRegistrationRequest, HttpStatus.OK);
        performPostRequest(userRegistrationRequest, HttpStatus.BAD_REQUEST);
    }

    private void performPostRequest(UserRegistrationRequest request, HttpStatus expectedStatus) throws Exception {
        String userJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(expectedStatus.value()));
    }
}
