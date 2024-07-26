package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.dto.*;
import com.revature.services.hashutil.PasswordEncrypter;
import com.revature.models.Users;
import com.revature.repositories.EducatorRepository;
import com.revature.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.google.gson.JsonObject;
import org.javatuples.Pair;

@Service
public class UserService {

    @Autowired
    JwtService js;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    EducatorRepository educatorRepository;

       
    public SignUpOutput signUpUser(Users user) {
        boolean signUpStatus = true;
        String signUpStatusMessage = "";

        int newUserId = user.getUserId();
        String newEmail =user.getEmail();
        String role = user.getRole();
        
        if (role.equalsIgnoreCase("student")) {
        if(newEmail==null)
        {
            signUpStatus = false;
            signUpStatusMessage = "Please enter A valid Email";

            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }

        Users existingUser = studentRepository.findByUserId(newUserId);
        
  
        if(existingUser!=null)
        {
            signUpStatus = false;
            signUpStatusMessage = "Email already Registered";

            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
        try
        {
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getPassword());
            user.setPassword(encryptedPassword);
           
            studentRepository.save(user);

            signUpStatusMessage = "New Customer Registered";
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
        catch (Exception e){
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);

        }}
        
        else if (role.equalsIgnoreCase("educator")) {
        
        Users existingUser = educatorRepository.findByUserId(newUserId);
        
        
        if(existingUser!=null)
        {
            signUpStatus = false;
            signUpStatusMessage = "Email already Registered";

            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
        try
        {
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getPassword());
            user.setPassword(encryptedPassword);
           
            educatorRepository.save(user);

            signUpStatusMessage = "New User Registered";
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
        catch (Exception e){
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);

        }}
		return null;

    }

    public Pair<Integer,String> signInUser(SignInInput signInInput) {

        String userEmail = signInInput.getEmail();
        String signInStatusMessage = "";
        if(userEmail ==null)
        {
           
            JsonObject message = new JsonObject();
            message.addProperty("message","Please Enter a Valid Email");
            return new Pair<>(401,message.toString());
            
        }


        Users existingUser = StudentRepository.findByEmail(signInInput.getEmail());

        if(existingUser==null)
        {
            
            JsonObject message = new JsonObject();
            message.addProperty("message","Email not registered");
            return new Pair<>(401,message.toString());
            
        }
       
        try{
            String encryptedPass = PasswordEncrypter.encryptPassword(signInInput.getPassword());

            if(existingUser.getPassword().equals(encryptedPass))
            {
                
          
                signInStatusMessage = "Signed In Successfully";
                String jwt = js.generateJwt(existingUser.getUserId());
                JsonObject jwtjson = new JsonObject();
                jwtjson.addProperty("jwt",jwt);
                return new Pair<>(200,jwtjson.toString());        //return created jwt to user
            }
            else
            {

                JsonObject message = new JsonObject();
                message.addProperty("message","Invalid Credentials");
                return new Pair<>(401,message.toString());
            }
        }
        catch (Exception e)
        {
            JsonObject message = new JsonObject();
            message.addProperty("message","Internal error occurred during Sign in");
            return new Pair<>(401,message.toString());
           
        }
    }
    
    public Users getUserId(Integer userId) {

        if (userId == null || !studentRepository.existsById(userId)) {
            throw new BadRequestException("User Id is invalid.");
        }
        return studentRepository.findByUserId(userId);
    }
}