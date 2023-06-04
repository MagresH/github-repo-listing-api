package com.example.githubrepolistingapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubUser {
    private String login;
    private int id;
    private String repos_url;
    private String html_url;
}
