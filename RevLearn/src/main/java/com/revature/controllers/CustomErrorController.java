package com.revature.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", request.getAttribute("javax.servlet.error.status_code"));
        response.put("message", request.getAttribute("javax.servlet.error.message"));
        return new ResponseEntity<>(response, HttpStatus.valueOf((Integer) request.getAttribute("javax.servlet.error.status_code")));
    }

    public String getErrorPath() {
        return "/error";
    }
}