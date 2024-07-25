package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.dto.*;
import com.revature.services.hashutil.PasswordEncrypter;
import com.revature.models.Users;
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
    StudentRepository userRepository;

       
    public SignUpOutput signUpUser(Users user) {
        boolean signUpStatus = true;
        String signUpStatusMessage = "";

        String newEmail = user.getEmail();
        

        if(newEmail==null)
        {
            signUpStatus = false;
            signUpStatusMessage = "Please enter A valid Email";

            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }

        Users existingUser = userRepository.findFirstByUserEmail(newEmail);
        
  
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
           
            userRepository.save(user);

            signUpStatusMessage = "New Customer Registered";
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
        catch (Exception e){
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);

        }

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


        Users existingUser = userRepository.findFirstByUserEmail(signInInput.getEmail());

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
                String jwt = js.generateJwt(existingUser.getUserId() , existingUser.getEmail(), existingUser.getRole());
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
    public Users findFirstByUserEmail(String email) {
        return userRepository.findFirstByUserEmail(email);
    }
}