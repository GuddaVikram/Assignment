package com.sunbaseData.assignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @JsonProperty("login_id")
    private String loginId;
    @JsonProperty("password")
    private String password;
}
