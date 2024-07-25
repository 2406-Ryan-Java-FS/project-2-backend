package com.revature.services;

import com.revature.models.Users;

public interface JwtService {
    public String generateJwt(Integer id);
    public Users getUserFromToken(String token);
    public int verifyJwt(String token);
}