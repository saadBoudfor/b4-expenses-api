package fr.b4.apps.clients.web;

import fr.b4.apps.clients.entities.User;
import fr.b4.apps.clients.repositories.UserRepository;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Users API", tags = "Users", protocols = "http")
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Get list of users (clients) in the System ", response = User[].class, tags = "Users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = User[].class) })
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @ApiOperation(value = "Add new users to database", response = User[].class, tags = "Users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User added success", response = User.class) })
    @PostMapping
    public User save(@RequestBody User toSave) {
        return userRepository.save(toSave);
    }
}
