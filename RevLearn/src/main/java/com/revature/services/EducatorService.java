package com.revature.services;

import com.revature.models.Educator;

public interface EducatorService {

    Educator addEducator(Educator educator);

    Educator getEducator(Integer userId);
}
