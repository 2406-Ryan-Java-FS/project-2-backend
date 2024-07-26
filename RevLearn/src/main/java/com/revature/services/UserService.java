package com.revature.services;

import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.dtos.UserEducator;

public interface UserService {
    User addUser(User user);

    User getUser(Integer userId);

    boolean isUser(Integer userId);

    Integer verifyUser(User user);

    UserEducator combineUserAndEducator(User user, Educator educator);
}
