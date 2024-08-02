package com.revature.services;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.revature.repositories.CourseRepository;

public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;
}