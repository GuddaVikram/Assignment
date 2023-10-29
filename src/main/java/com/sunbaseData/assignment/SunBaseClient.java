

package com.sunbaseData.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunbaseData.assignment.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SunBaseClient {
    @Value("${api.sunBase.url}")
    private String sunBaseUrl;

    @Autowired
    private ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public LoginResponse getAccessToken(LoginRequest loginRequest) throws JsonProcessingException {
        try {
            URI requestUri = UriComponentsBuilder.fromUriString(sunBaseUrl)
                    .path("assignment_auth.jsp")
                    .build()
                    .toUri();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(loginRequest)))
                            .headers("Content-Type", "")
                            .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), LoginResponse.class);
            } else {
                throw new RuntimeException("Failed to retrieve access token. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> createCustomer(String authToken, CreateCustomerRequest createCustomerRequest) throws JsonProcessingException {
        try {
            URI requestUri = UriComponentsBuilder.fromUriString(sunBaseUrl)
                    .path("assignment.jsp")
                    .queryParam("cmd", "create")
                    .build()
                    .toUri();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(createCustomerRequest)))
                            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + authToken)
                            .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new RuntimeException("Failed to create a customer. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Customer> getCustomerList(String authToken) throws JsonProcessingException {
        try {
            URI requestUri = UriComponentsBuilder.fromUriString(sunBaseUrl)
                    .path("assignment.jsp")
                    .queryParam("cmd", "get_customer_list")
                    .build()
                    .toUri();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .GET()
                    .headers("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .headers("Authorization", "Bearer " + authToken)
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<Customer>>() {});
            } else {
                throw new RuntimeException("Failed to get customer list. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> deleteCustomer(String authToken, DeleteCustomerReq deleteCustomerReq) {
        try {
            URI requestUri = UriComponentsBuilder.fromUriString(sunBaseUrl)
                    .path("assignment.jsp")
                    .queryParam("cmd", "delete")
                    .queryParam("uuid", deleteCustomerReq.getUuid())
                    .build()
                    .toUri();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .headers("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .headers("Authorization", "Bearer " + authToken)
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new RuntimeException("Failed to delete the customer. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> updateCustomer(String authToken, Customer customer) throws JsonProcessingException {
        try {
            URI requestUri = UriComponentsBuilder.fromUriString(sunBaseUrl)
                    .path("assignment.jsp")
                    .queryParam("cmd", "update")
                    .queryParam("uuid", customer.getUuid())
                    .build()
                    .toUri();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(customer)))
                            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + authToken)
                            .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new RuntimeException("Failed to update the customer. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer getCustomerByID(String customerId, String authToken) throws JsonProcessingException {
        List<Customer> customerList = getCustomerList(authToken);
        Optional<Customer> foundCustomer = customerList.stream().filter(customer -> customerId.equals(customer.getUuid())).findFirst();
        return foundCustomer.orElse(null);
    }
}
