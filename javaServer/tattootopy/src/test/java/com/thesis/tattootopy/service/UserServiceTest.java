package com.thesis.tattootopy.service;

import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.model.User;
import com.thesis.tattootopy.persistance.interfaces.IUserRepository;
import com.thesis.tattootopy.service.implementation.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    IUserRepository userRepository;

    @Test
    public void getAllUsersTest() {
        List<User> list = new ArrayList<>();
        User user1 = new User("mara.mara@gmail.com", "aaa");
        User user2 = new User("ana.ana@gmail.com", "bbb");
        User user3 = new User("george.ana@gmail.com", "ccc");
        User user4 = new User("catalina.ana@gmail.com", "ddd");

        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);

        when(userRepository.findAll()).thenReturn(list);

        // ---------- GET ALL USERS
        List<User> userList = userService.getAllUsers();

        assertEquals(4, userList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void findAllTattoosOfUserTest_ExistingEmail() {
        User user = new User("mara.mara@gmail.com", "aaa");
        Tattoo tattoo1 = new Tattoo("cat");
        tattoo1.setId(1L);
        Tattoo tattoo2 = new Tattoo("doggo");
        tattoo2.setId(2L);

        user.addTattoo(tattoo1);
        user.addTattoo(tattoo2);

        List<Tattoo> initialTattoos = new ArrayList<>();
        initialTattoos.add(tattoo1);
        initialTattoos.add(tattoo2);

        when(userRepository.findById("mara.mara@gmail.com")).thenReturn(Optional.of(user));

        // ---------- find All Tattoos Of User
        List<Tattoo> returned = userService.findAllTattoosOfUser("mara.mara@gmail.com");

        assertEquals(returned, initialTattoos);

        verify(userRepository, times(1)).findById("mara.mara@gmail.com");
    }


    @Test
    public void findUserByEmailTest_ExistingEmail() {
        User user1 = new User("mara.mara@gmail.com", "aaa");
        User user2 = new User("ana.ana@gmail.com", "bbb");

        when(userRepository.findById("mara.mara@gmail.com")).thenReturn(Optional.of(user1));
        when(userRepository.findById("ana.ana@gmail.com")).thenReturn(Optional.of(user2));

        // ---------- find User By Email
        User returned1 = userService.findUserByEmail("mara.mara@gmail.com");
        User returned2 = userService.findUserByEmail("ana.ana@gmail.com");

        assertEquals(returned1, user1);
        assertEquals(returned2, user2);

        verify(userRepository, times(1)).findById("mara.mara@gmail.com");
        verify(userRepository, times(1)).findById("ana.ana@gmail.com");
    }

    @Test
    public void findUserByEmailTest_NonExistingEmail() {
        when(userRepository.findById("mara.mara@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findById("ana.ana@gmail.com")).thenReturn(Optional.empty());

        // ---------- find User By Email
        User returned1 = userService.findUserByEmail("mara.mara@gmail.com");
        User returned2 = userService.findUserByEmail("ana.ana@gmail.com");

        assertNull(returned1);
        assertNull(returned2);

        verify(userRepository, times(1)).findById("mara.mara@gmail.com");
        verify(userRepository, times(1)).findById("ana.ana@gmail.com");
    }

    // adding new users to the DB, should return true
    @Test
    public void saveUserTest_NewUser() {
        User user1 = new User("mara.mara@gmail.com", "aaa");
        User user2 = new User("ana.ana@gmail.com", "bbb");

        when(userRepository.findById("mara.mara@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findById("ana.ana@gmail.com")).thenReturn(Optional.empty());

        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.save(user2)).thenReturn(user2);

        // ---------- save User
        boolean returned1 = userService.save(user1);
        boolean returned2 = userService.save(user2);

        assertTrue(returned1);
        assertTrue(returned2);

        verify(userRepository, times(1)).findById("mara.mara@gmail.com");
        verify(userRepository, times(1)).findById("ana.ana@gmail.com");
        verify(userRepository, times(1)).save(user1);
        verify(userRepository, times(1)).save(user2);
    }

    // if the user already exists in the DB, should return false
    @Test
    public void saveUserTest_ExistingUser() {
        User user1 = new User("mara.mara@gmail.com", "aaa");

        when(userRepository.findById("mara.mara@gmail.com")).thenReturn(Optional.of(user1));

        // ---------- save User
        boolean returned = userService.save(user1);
        assertFalse(returned);

        verify(userRepository, times(1)).findById("mara.mara@gmail.com");
        verify(userRepository, times(0)).save(user1);
    }

    // updating an already existing user should return true
    @Test
    public void updateUserTest_ExistingUser() {
        User user = new User("mara.mara@gmail.com", "aaa", "first name", "last name");

        when(userRepository.getById("mara.mara@gmail.com")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // ---------- update User
        boolean returned = userService.update(user);
        assertTrue(returned);

        verify(userRepository, times(1)).getById("mara.mara@gmail.com");
        verify(userRepository, times(1)).save(user);
    }

    // deleting a user that exists
    @Test
    public void deleteUserTest_ExistedUser() {
        User user = new User("mara.mara@gmail.com", "aaa");

        when(userRepository.findById("mara.mara@gmail.com")).thenReturn(Optional.of(user));

        // ---------- delete User
        boolean returned = userService.deleteUserByEmail("mara.mara@gmail.com");
        assertTrue(returned);

        verify(userRepository, times(1)).findById("mara.mara@gmail.com");
        verify(userRepository, times(1)).deleteById("mara.mara@gmail.com");
    }

    // deleting a non-existing user
    @Test
    public void deleteUserTest_NonExistingUser() {
        when(userRepository.findById("mara.mara@gmail.com")).thenReturn(Optional.empty());

        // ---------- delete User
        boolean returned = userService.deleteUserByEmail("mara.mara@gmail.com");
        assertFalse(returned);

        verify(userRepository, times(1)).findById("mara.mara@gmail.com");
        verify(userRepository, times(0)).deleteById("mara.mara@gmail.com");
    }
}
