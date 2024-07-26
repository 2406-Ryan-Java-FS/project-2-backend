package com.revature.models.dtos;

import com.revature.models.Educator;
import com.revature.models.User;
import lombok.Data;

@Data
public class UserEducator {
    private User user;
    private Educator educator;
}
