package com.login.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GithubResponseDTO {
    private Long id;
    private String login;
    private String name;
    private String type;
    private String avatar_url;
    private String created_at;
    private int followers;
    private int public_repos;
    private String message;
}
