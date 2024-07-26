package com.revature.services;

import com.revature.DTO.UserEducatorDTO;
import com.revature.models.Educator;
import com.revature.models.User;

public interface UserService {
    User addUser(User user);

    User getUser(Integer userId);

    boolean isUser(Integer userId);

    Integer verifyUser(User user);

    UserEducatorDTO combineUserAndEducator(User user, Educator educator);
}
