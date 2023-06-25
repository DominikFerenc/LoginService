package com.login.service;

import com.login.client.ApiClient;
import com.login.dto.GithubResponseDTO;
import com.login.dto.UserDTO;
import com.login.model.User;
import com.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ApiClient apiClient;

    private final UserRepository userRepository;
    Logger logger = Logger.getLogger(UserService.class.getName());

    public UserDTO login(String login) {
        var githubResponseDTO = apiClient.getUser(login);
        float result = getCalculationResult(githubResponseDTO.getFollowers(),
                githubResponseDTO.getPublic_repos());

        incrementRequestCount(login, githubResponseDTO);

        return buildUserDto(githubResponseDTO, result);
    }

    public float getCalculationResult(int followers, int publicRepos) {
        var result = 6f / followers * (2 + publicRepos);
        if (result == Float.POSITIVE_INFINITY
                || result == Float.NEGATIVE_INFINITY) {
            logger.log(Level.WARNING, "ArithmeticException - Division by zero");
        }
        return result;
    }

    private UserDTO buildUserDto(GithubResponseDTO githubResponseDTO, float result) {
        return UserDTO.builder()
                .id(githubResponseDTO.getId())
                .login(githubResponseDTO.getLogin())
                .name(githubResponseDTO.getName())
                .type(githubResponseDTO.getType())
                .avatarUrl(githubResponseDTO.getAvatar_url())
                .createdAt(githubResponseDTO.getCreated_at())
                .calculations(result)
                .build();
    }

    private User buildUserEntity(GithubResponseDTO githubResponseDTO) {
        return User.builder()
                .login(githubResponseDTO.getLogin())
                .requestCount(1)
                .build();
    }

    public void incrementRequestCount(String login, GithubResponseDTO githubResponseDTO) {
        List<User> users = userRepository.findByLogin(login);
        if (!users.isEmpty()) {
            User user = users.get(0);
            user.setRequestCount(user.getRequestCount() + 1);
            userRepository.save(user);
        } else {
            userRepository.save(buildUserEntity(githubResponseDTO));
        }

    }
}
