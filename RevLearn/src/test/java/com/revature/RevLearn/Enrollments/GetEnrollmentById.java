package com.revature.RevLearn.Enrollments;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.RevLearnApplication;
import com.revature.models.Enrollment;
import com.revature.models.enums.PayStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GetEnrollmentById {

    ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objMapper;


    @BeforeEach
    public void setup() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
        objMapper = new ObjectMapper();
        String[] args = new String[] {};
        app = SpringApplication.run(RevLearnApplication.class, args);
        Thread.sleep(500);
    }


    @AfterEach
    public void tearDown() throws InterruptedException{
        Thread.sleep(500);
        SpringApplication.exit(app);
    }


    @Test
    public void getEnrollmentsByStatusAndStudentId_200success() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/4"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();;
        Assertions.assertEquals(200, status, "Expected 200 - Actual was: " + status);

        Enrollment expectedResult = new Enrollment(4, 2, 4, Timestamp.valueOf("2024-07-29 15:01:26.464"),
                PayStatus.pending, false,  3, null);

        Enrollment actualResult = objMapper.readValue(response.body().toString(), new TypeReference<Enrollment>(){});
        Assertions.assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }


    @Test
    public void getEnrollmentsByStatusAndStudentId_404notFound_idDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/10"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();;
        Assertions.assertEquals(404, status, "Expected 404 - Actual was: " + status);

        String expectedResult = "Enrollment Record with ID: 10 could not be found";

        String actualResult = response.body().toString();
        Assertions.assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }
}
