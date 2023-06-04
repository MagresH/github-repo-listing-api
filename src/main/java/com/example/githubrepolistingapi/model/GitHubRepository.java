package com.example.githubrepolistingapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubRepository {
    private String name;
    private GitHubRepositoryOwner owner;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean fork;

    private List<GitHubBranch> branches;

}
