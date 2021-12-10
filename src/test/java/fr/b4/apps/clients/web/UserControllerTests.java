package fr.b4.apps.clients.web;

import fr.b4.apps.DataGenerator;
import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {
    @Mock
    UserRepository userRepository;

    @Test
    public void shouldGetUsersSuccess() {
        List<User> users = DataGenerator.generateUser(8);
        when(userRepository.findAll()).thenReturn(users);
        UserController userController = new UserController(userRepository);
        List<User> found = userController.getAllUsers();
        Assertions.assertEquals(found, users);
    }

    @Test
    public void shouldSaveUserSuccess() {
        User user = DataGenerator.generateUser(1).get(0);
        when(userRepository.save(any())).thenReturn(user);
        UserController userController = new UserController(userRepository);
        User saved = userController.save(user);
        Assertions.assertEquals(saved, user);
    }
}
