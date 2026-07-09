package gitex.gitdemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setId(1L);
        user.setName("Rafi");
        user.setEmail("rafi@gmail.com");
    }

    @Test
    void testGetAllUsers() {

        when(userRepository.findAll())
                .thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Rafi", users.get(0).getName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User found = userService.getUserById(1L);

        assertNotNull(found);
        assertEquals("Rafi", found.getName());

        verify(userRepository).findById(1L);
    }

    @Test
    void testCreateUser() {

        when(userRepository.save(user))
                .thenReturn(user);

        User saved = userService.createUser(user);

        assertEquals("Rafi", saved.getName());

        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser() {

        User updated = new User();

        updated.setName("Virat");
        updated.setEmail("virat@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updated);

        assertEquals("Virat", result.getName());
        assertEquals("virat@gmail.com", result.getEmail());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser() {

        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> userService.getUserById(1L));

        assertEquals("User not found: 1", ex.getMessage());

        verify(userRepository).findById(1L);
    }
}