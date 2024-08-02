package com.revature.RevLearn.Courses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.RevLearnApplication;
import com.revature.models.Course;
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

public class GetCoursesByEducatorId {

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
    public void getCoursesByEducatorId_200successful_populated() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/courses/educators/4"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();;
        Assertions.assertEquals(200, status, "Expected 200 - Actual was: " + status);

        List<Course> expectedResult = new ArrayList<>();
        expectedResult.add(new Course(
                3,
                4,
                "Calculus I",
                "An introductory course on calculus.",
                "Mathematics",
                59.99,
                "https://www.fourpaws.com/-/media/Project/OneWeb/FourPaws/Images/articles/cat-corner/cats-that-dont-shed/siamese-cat.jpg",
                Timestamp.valueOf("2024-07-29 15:01:26.460")));
        expectedResult.add(new Course(
                4,
                4,
                "Statistics",
                "Basic statistics concepts and techniques.",
                "Mathematics",
                69.99,
                "https://www.fourpaws.com/-/media/Project/OneWeb/FourPaws/Images/articles/cat-corner/cats-that-dont-shed/siamese-cat.jpg",
                Timestamp.valueOf("2024-07-29 15:01:26.460")
        ));

        List<Course> actualResult = objMapper.readValue(response.body().toString(), new TypeReference<List<Course>>(){});
        Assertions.assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }



    @Test
    public void getCoursesByEducatorId_200successful_empty() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/courses/educators/40"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected 200 - Actual was: " + status);

        List<Course> expectedResult = new ArrayList<>();


        List<Course> actualResult = objMapper.readValue(response.body().toString(), new TypeReference<List<Course>>(){});
        Assertions.assertEquals(expectedResult, actualResult, "Expected=" + expectedResult + ", Actual=" + actualResult);
    }
}
