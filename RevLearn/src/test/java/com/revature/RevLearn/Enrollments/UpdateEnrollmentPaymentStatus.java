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

public class UpdateEnrollmentPaymentStatus {

    ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objMapper;

    @BeforeEach
    public void setup() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
        objMapper = new ObjectMapper();
        String[] args = new String[]{};
        app = SpringApplication.run(RevLearnApplication.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(500);
        SpringApplication.exit(app);
    }

    @Test
    public void updateEnrollmentPaymentStatus_200success() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/payStatus/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"payStatus\": \"pending\" }"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(200, status, "Expected 200 - Actual " + status);

        Enrollment expected = new Enrollment(
                1,
                1,
                1,
                Timestamp.valueOf("2024-07-29 15:01:26.464"),
                PayStatus.pending,
                true,
                3,
                "Great course!"
        );

        Enrollment actual = objMapper.readValue(response.body(), new TypeReference<Enrollment>() {});
        Assertions.assertEquals(expected, actual, "Expected=" + expected + ", Actual=" + actual);
    }

    @Test
    public void updateEnrollmentPaymentStatus_404idNotFound() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/payStatus/1000"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"payStatus\": \"cancelled\" }"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(404, status, "Expected 404 - Actual " + status);

        String expected = "Enrollment with Id: 1000 does not exist";

        String actual = response.body();
        Assertions.assertEquals(expected, actual, "Expected=" + expected + ", Actual=" + actual);
    }

    @Test
    public void updateEnrollmentPaymentStatus_400jsonPropertyIncorrect() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/payStatus/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"status\": \"cancelled\" }"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(400, status, "Expected 400 - Actual " + status);

        String expected = "Could not update payment status";

        String actual = response.body();
        Assertions.assertEquals(expected, actual, "Expected=" + expected + ", Actual=" + actual);
    }

    @Test
    public void updateEnrollmentPaymentStatus_400invalidStatus() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/enrollments/payStatus/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"payStatus\": \"pineapple\" }"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(400, status, "Expected 400 - Actual " + status);

        String expected = "Please enter 'pending', 'completed', or 'cancelled'";

        String actual = response.body();
        Assertions.assertEquals(expected, actual, "Expected=" + expected + ", Actual=" + actual);
    }
}

