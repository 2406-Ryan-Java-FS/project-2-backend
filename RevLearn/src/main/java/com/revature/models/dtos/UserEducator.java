package com.revature.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.revature.models.Educator;
import com.revature.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEducator {
    private String token;           //JWT
    private User user=new User();   //added new() to see the entire shape of this model in json
    private Educator educator=new Educator();
}
