package com.revature.RevLearn.Enrollments;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.RevLearnApplication;
import com.revature.models.Enrollment;
import com.revature.models.enums.PayStatus;
import org.junit.jupiter.api.*;
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


public class GetEnrollmentsByStatus {


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
    public void getEnrollmentsByStatus_200success_populated() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/status/pending"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();;
        Assertions.assertEquals(200, status, "Expected 200 - Actual was: " + status);

        List<Enrollment> expectedResult = new ArrayList<>();
        expectedResult.add(new Enrollment(4, 2, 4, Timestamp.valueOf("2024-07-29 15:01:26.464"),
                PayStatus.pending, false,  3, null));

        List<Enrollment> actualResult = objMapper.readValue(response.body().toString(), new TypeReference<List<Enrollment>>(){});
        Assertions.assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }

    @Test
    public void getEnrollmentsByStatus_200success_unpopulated() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/status/cancelled"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();;
        Assertions.assertEquals(200, status, "Expected 200 - Actual was: " + status);

        List<Enrollment> expectedResult = new ArrayList<>();

        List<Enrollment> actualResult = objMapper.readValue(response.body().toString(), new TypeReference<List<Enrollment>>(){});
        Assertions.assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }

    @Test
    public void getEnrollmentsByStatus_400badRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/status/asd"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();;
        Assertions.assertEquals(400, status, "Expected 400 - Actual was: " + status);

        String expectedResult = "Invalid Status";

        String actualResult = response.body().toString();
        Assertions.assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }

}
