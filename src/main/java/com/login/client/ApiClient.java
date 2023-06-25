package com.login.client;

import com.login.dto.GithubResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class ApiClient {
    private final RestTemplate restTemplate;

    public GithubResponseDTO getUser(String login) {
        ResponseEntity<GithubResponseDTO> response;
        final String endpointUserGithub = "https://api.github.com/users/" + login;
        response = restTemplate.getForEntity(endpointUserGithub, GithubResponseDTO.class);
        return response.getBody();
    }
}
