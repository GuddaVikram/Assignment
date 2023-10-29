package com.sunbaseData.assignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {
    @NotNull(message = "first_name is mandatory")
    @JsonProperty("first_name")
    private String firstName;
    @NotNull(message = "last_name is mandatory")
    @JsonProperty("last_name")
    private String lastName;
    private String street;
    private String address;
    private String city;
    private String state;
    @Email(message = "enter a valid email")
    private String email;
    private String phone;


}
