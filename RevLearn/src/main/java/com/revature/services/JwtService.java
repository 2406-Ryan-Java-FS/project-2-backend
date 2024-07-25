package com.revature.services;

import com.revature.models.User;

public interface JwtService {
    public String generateJwt(Integer id);
    public User getUserFromToken(String token);
    public int verifyJwt(String token);
}
