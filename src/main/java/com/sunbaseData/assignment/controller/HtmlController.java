package com.sunbaseData.assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sunbaseData.assignment.SunBaseClient;
import com.sunbaseData.assignment.models.LoginRequest;
import com.sunbaseData.assignment.models.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/html")
public class HtmlController {

    @Autowired
    private SunBaseClient sunBaseClient;




}
