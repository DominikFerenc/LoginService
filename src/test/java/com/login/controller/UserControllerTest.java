package com.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.dto.UserDTO;
import com.login.exceptions.UserExceptionHandler;
import com.login.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new UserExceptionHandler())
                .build();
    }

    @Test
    public void testGetUserSuccess() throws Exception {
        final String login = "octocat";
        UserDTO userDto = buildUserDto(login);
        when(userService.login(login)).thenReturn(userDto);


        MvcResult result = mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.not(Matchers.isEmptyOrNullString())))
                .andReturn();


        String responseJson = result.getResponse().getContentAsString();
        UserDTO responseDto = new ObjectMapper().readValue(responseJson, UserDTO.class);
        Assert.assertEquals(login, responseDto.getLogin());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        String login = "aa83534";
        String errorBody = "Login " + login + " not found";
        when(userService.login(login)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, errorBody));


        MvcResult result = mockMvc.perform(get("/users/{login}", login))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorBody))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assert.assertEquals(errorBody, responseBody);
    }

    private UserDTO buildUserDto(String login) {
        return UserDTO.builder()
                .login(login)
                .type("user")
                .name("name")
                .createdAt("12.12.1222")
                .avatarUrl("test")
                .calculations(12)
                .build();
    }
}