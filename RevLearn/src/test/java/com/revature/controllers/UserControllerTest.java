package com.revature.controllers;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.dtos.UserEducator;
import com.revature.models.dtos.UserToken;
import com.revature.models.enums.Role;
import com.revature.services.EducatorService;
import com.revature.services.JwtService;
import com.revature.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private EducatorService educatorService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUserSuccessStudent() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setRole(Role.student);

        UserEducator mockUserEducator = new UserEducator();
        mockUserEducator.setUser(mockUser);

        when(userService.addUser(any(User.class))).thenReturn(mockUser);

        ResponseEntity<Object> response = userController.addUser(mockUserEducator);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser.getUserId(), response.getBody());
    }

    @Test
    void addUserSuccessEducator() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setRole(Role.educator);

        Educator mockEducator = new Educator();
        mockEducator.setEducatorId(1);

        UserEducator mockUserEducator = new UserEducator();
        mockUserEducator.setUser(mockUser);
        mockUserEducator.setEducator(mockEducator);

        when(userService.addUser(any(User.class))).thenReturn(mockUser);
        when(educatorService.addEducator(any(Educator.class))).thenReturn(mockEducator);

        ResponseEntity<Object> response = userController.addUser(mockUserEducator);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockEducator.getEducatorId(), response.getBody());
    }

    @Test
    void addUserConflictException() {
        UserEducator mockUserEducator = new UserEducator();
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUserEducator.setUser(mockUser);

        when(userService.addUser(any(User.class))).thenThrow(new ConflictException("Email already exists."));

        ResponseEntity<Object> response = userController.addUser(mockUserEducator);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists.", response.getBody());
    }

    @Test
    void loginUserSuccess() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
        mockUser.setUserId(1);

        UserToken mockUserToken = new UserToken("mockToken", mockUser);

        when(userService.verifyUser(any(User.class))).thenReturn(mockUser.getUserId());
        when(jwtService.generateJwt(any(Integer.class))).thenReturn("mockToken");
        when(userService.getUser(any(Integer.class))).thenReturn(mockUser);

        ResponseEntity<Object> response = userController.loginUser(mockUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserToken, response.getBody());
    }

    @Test
    void loginUserUnauthorizedException() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("wrongpassword");

        when(userService.verifyUser(any(User.class))).thenThrow(new UnauthorizedException("Invalid login credentials"));

        ResponseEntity<Object> response = userController.loginUser(mockUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid login credentials", response.getBody());
    }

    @Test
    void getCurrentUserSuccessStudent() {
        String mockJwt = "mockJwtToken";
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setRole(Role.student);

        when(jwtService.getUserFromToken(any(String.class))).thenReturn(mockUser);

        ResponseEntity<Object> response = userController.getCurrentUser(mockJwt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void getCurrentUserSuccessEducator() {
        String mockJwt = "mockJwtToken";
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setRole(Role.educator);

        Educator mockEducator = new Educator();
        mockEducator.setEducatorId(1);

        UserEducator mockUserEducator = new UserEducator();
        mockUserEducator.setUser(mockUser);
        mockUserEducator.setEducator(mockEducator);

        when(jwtService.getUserFromToken(any(String.class))).thenReturn(mockUser);
        when(educatorService.getEducator(any(Integer.class))).thenReturn(mockEducator);
        when(userService.combineUserAndEducator(any(User.class), any(Educator.class))).thenReturn(mockUserEducator);

        ResponseEntity<Object> response = userController.getCurrentUser(mockJwt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserEducator, response.getBody());
    }

    @Test
    void getCurrentUserBadRequestException() {
        String mockJwt = "invalidJwtToken";

        when(jwtService.getUserFromToken(any(String.class))).thenThrow(new BadRequestException("Invalid token."));

        ResponseEntity<Object> response = userController.getCurrentUser(mockJwt);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid token.", response.getBody());
    }
}
