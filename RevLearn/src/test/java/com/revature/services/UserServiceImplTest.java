package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.dtos.UserEducator;
import com.revature.models.enums.Role;
import com.revature.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUser_success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(Role.student);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void addUser_conflict() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.addUser(user));
    }

    @Test
    void addUser_badRequest_missingEmail() {
        User user = new User();
        user.setPassword("password123");

        assertThrows(BadRequestException.class, () -> userService.addUser(user));
    }

    @Test
    void addUser_badRequest_missingPassword() {
        User user = new User();
        user.setEmail("test@example.com");

        assertThrows(BadRequestException.class, () -> userService.addUser(user));
    }

    @Test
    void addUser_success_withEducatorRole() {
        User user = new User();
        user.setEmail("educator@example.com");
        user.setPassword("password123");
        user.setRole(Role.educator);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals(Role.educator, savedUser.getRole());
    }

    @Test
    void getUser_success() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");

        when(userRepository.existsById(user.getUserId())).thenReturn(true);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(user);

        User foundUser = userService.getUser(1);

        assertNotNull(foundUser);
        assertEquals(user.getUserId(), foundUser.getUserId());
        verify(userRepository).existsById(user.getUserId());
        verify(userRepository).findByUserId(user.getUserId());
    }

    @Test
    void getUser_userNotFound() {
        when(userRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.getUser(999));
    }

    @Test
    void getUser_badRequest() {
        assertThrows(BadRequestException.class, () -> userService.getUser(null));
        assertThrows(BadRequestException.class, () -> userService.getUser(1));
    }

    @Test
    void verifyUser_success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password123");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(existingUser);

        Integer userId = userService.verifyUser(user);

        assertNotNull(userId);
        assertEquals(existingUser.getUserId(), userId);
    }

    @Test
    void verifyUser_unauthorized() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("wrongPassword");

        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password123");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(existingUser);

        assertThrows(UnauthorizedException.class, () -> userService.verifyUser(user));
    }

    @Test
    void verifyUser_noUserFound() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> userService.verifyUser(user));
    }

    @Test
    void combineUserAndEducator_success() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");

        Educator educator = new Educator();

        UserEducator dto = userService.combineUserAndEducator(user, educator);

        assertNotNull(dto);
        assertEquals(user, dto.getUser());
        assertEquals(educator, dto.getEducator());
        assertEquals(user.getUserId(), dto.getEducator().getEducatorId());
    }

    @Test
    void combineUserAndEducator_nullEducator() {
        User user = new User();
        user.setUserId(1);

        UserEducator dto = userService.combineUserAndEducator(user, null);

        assertNotNull(dto);
        assertEquals(user, dto.getUser());
        assertNull(dto.getEducator());
    }

    @Test
    void isUser_exists() {
        when(userRepository.existsById(anyInt())).thenReturn(true);

        assertTrue(userService.isUser(1));
    }

    @Test
    void isUser_notExists() {
        when(userRepository.existsById(anyInt())).thenReturn(false);

        assertFalse(userService.isUser(1));
    }
}
