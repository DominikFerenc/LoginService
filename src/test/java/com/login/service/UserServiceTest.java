package com.login.service;

import com.login.client.ApiClient;
import com.login.dto.GithubResponseDTO;
import com.login.dto.UserDTO;
import com.login.model.User;
import com.login.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        String login = "octocat";

        GithubResponseDTO githubResponseDTO = new GithubResponseDTO();
        githubResponseDTO.setId(1L);
        githubResponseDTO.setLogin(login);
        githubResponseDTO.setName("Octocat");
        githubResponseDTO.setType("user");
        githubResponseDTO.setAvatar_url("https://github.com/octocat.png");
        githubResponseDTO.setCreated_at("2021-01-01T12:00:00Z");
        githubResponseDTO.setFollowers(100);
        githubResponseDTO.setPublic_repos(10);

        when(apiClient.getUser(login)).thenReturn(githubResponseDTO);

        UserDTO expectedUserDTO = buildExpectedUserDto(login);

        List<User> emptyUserList = new ArrayList<>();
        when(userRepository.findByLogin(login)).thenReturn(emptyUserList);

        User savedUser = buildSavedUser(login);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.login(login);

        assertEquals(expectedUserDTO, result);
        verify(apiClient).getUser(login);
        verify(userRepository).findByLogin(login);
        verify(userRepository).save(any(User.class));
    }


    @Test
    public void testLoginUserExists() {
        String login = "octocat";

        GithubResponseDTO githubResponseDTO = new GithubResponseDTO();
        githubResponseDTO.setId(1L);
        githubResponseDTO.setLogin(login);
        githubResponseDTO.setName("Octocat");
        githubResponseDTO.setType("user");
        githubResponseDTO.setAvatar_url("https://github.com/octocat.png");
        githubResponseDTO.setCreated_at("2021-01-01T12:00:00Z");
        githubResponseDTO.setFollowers(100);
        githubResponseDTO.setPublic_repos(10);

        when(apiClient.getUser(login)).thenReturn(githubResponseDTO);

        UserDTO expectedUserDTO = buildExpectedUserDto(login);

        User existingUser = buildExistingUser(login);

        List<User> userList = new ArrayList<>();
        userList.add(existingUser);

        when(userRepository.findByLogin(login)).thenReturn(userList);

        User savedUser = buildSavedCountUser(login);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.login(login);

        assertEquals(expectedUserDTO, result);
        verify(apiClient).getUser(login);
        verify(userRepository).findByLogin(login);
        verify(userRepository).save(any(User.class));
    }

    private UserDTO buildExpectedUserDto(String login) {
        return UserDTO.builder()
                .id(1L)
                .login(login)
                .name("Octocat")
                .type("user")
                .avatarUrl("https://github.com/octocat.png")
                .createdAt("2021-01-01T12:00:00Z")
                .calculations(6f / 100 * (2 + 10))
                .build();
    }

    private User buildSavedUser(String login) {
        return User.builder()
                .login(login)
                .requestCount(1)
                .build();
    }

    private User buildExistingUser(String login) {
        return User.builder()
                .login(login)
                .requestCount(2)
                .build();
    }

    private User buildSavedCountUser(String login) {
        return User.builder()
                .login(login)
                .requestCount(3)
                .build();
    }

}