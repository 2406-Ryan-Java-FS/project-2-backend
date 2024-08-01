package com.revature.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//General purpose body to respond with anything
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Body {

    private String message;
}
