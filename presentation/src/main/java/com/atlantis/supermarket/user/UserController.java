package com.atlantis.supermarket.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlantis.supermarket.core.user.User;
import com.atlantis.supermarket.core.user.UserService;
import com.atlantis.supermarket.core.user.exceptions.UserExistsException;

@RestController
@RequestMapping("/api/public")
public class UserController {

    @Autowired
    private UserService useCase;
    
    
    @GetMapping
    public String getMessage() {
        return "Hello from public API controller";
    }
    
    //https://medium.com/@MatthewFTech/spring-boot-cache-with-redis-56026f7da83a
    @CacheEvict(value="users")
    @PostMapping("/sign-up")
    public void signUp(@RequestBody User user) throws UserExistsException {
	useCase.createUser(user);
    }
    
    @Cacheable(value="users")
    @GetMapping("/users")
    public List<User> getUsers(){
	return useCase.findAll();
    }
}