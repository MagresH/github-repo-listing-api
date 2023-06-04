package com.example.githubrepolistingapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubBranch {
    private String name;
    private GitHubCommit commit;

}
