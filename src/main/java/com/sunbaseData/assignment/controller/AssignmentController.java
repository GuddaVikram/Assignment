package com.sunbaseData.assignment.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sunbaseData.assignment.SunBaseClient;
import com.sunbaseData.assignment.models.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {


    @Autowired
    SunBaseClient sunBaseClient;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    String login(LoginRequest loginRequest) throws JsonProcessingException {
        LoginResponse loginResponse =  sunBaseClient.getAccessToken(loginRequest);
        String accessToken  = loginResponse.getAccessToken();
        return "redirect:/assignment/customer/list?auth=" + accessToken;
    }




    @PostMapping("/customer/create/{authorizationToken}")
    String createCustomer(@PathVariable String authorizationToken, CreateCustomerRequest createCustomerRequest,Model model) throws JsonProcessingException {
         sunBaseClient.createCustomer(authorizationToken, createCustomerRequest);
         return "redirect:/assignment/customer/list?auth=" + authorizationToken;
    }

    @GetMapping("/customer/list")
    String getCustomerList(@RequestParam String auth, Model model) throws JsonProcessingException {
        List<Customer> customerList = sunBaseClient.getCustomerList(auth);
        model.addAttribute("customerList", customerList);
        model.addAttribute("authToken",auth);
        model.addAttribute("createCustomerRequest",new CreateCustomerRequest());
        return "customer-list";
    }


    @GetMapping("/customer/edit/{customerId}/{auth}")
    public String getCustomerToEdit(@PathVariable String customerId,@PathVariable String auth, Model model) throws JsonProcessingException {
        // Retrieve customer details by customerId and add to the model
        Customer customer = sunBaseClient.getCustomerByID(customerId,auth);
        // Render an edit form or page
        model.addAttribute("customer",customer);
        model.addAttribute("authToken",auth);
        return "edit-customer";
    }

    @GetMapping("/customer/delete/{customerId}/{auth}")
    public String deleteCustomer(@PathVariable String customerId,@PathVariable String auth, Model model) throws JsonProcessingException {
        // Retrieve customer details by customerId and add to the model
        DeleteCustomerReq deleteCustomerReq = new DeleteCustomerReq(customerId);
        sunBaseClient.deleteCustomer(auth,deleteCustomerReq);
        return "redirect:/assignment/customer/list?auth=" + auth;
    }



    @PostMapping("/customer/update/{authToken}")
    public String updateCustomer(@PathVariable String authToken, Customer customer) throws JsonProcessingException {
        // Update the customer with the edited data
        sunBaseClient.updateCustomer(authToken,customer);
        // Redirect to the customer list page or another appropriate page
        return "redirect:/assignment/customer/list?auth=" + authToken;
    }

}
