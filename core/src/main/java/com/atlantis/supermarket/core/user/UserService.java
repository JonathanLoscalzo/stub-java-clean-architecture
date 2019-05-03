package com.atlantis.supermarket.core.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atlantis.supermarket.common.parser.Encoder;
import com.atlantis.supermarket.core.shared.UseCase;
import com.atlantis.supermarket.core.user.event.UserCreatedEvent;
import com.atlantis.supermarket.core.user.exceptions.UserExistsException;
import com.atlantis.supermarket.core.user.exceptions.UsernameNotFoundException;
import com.atlantis.supermarket.core.user.generator.SaveUser;
import com.atlantis.supermarket.core.user.searcher.FindAll;
import com.atlantis.supermarket.core.user.searcher.FindUserByUsername;
import com.atlantis.supermarket.core.user.validation.UserExists;
import com.atlantis.supermarket.core.user.validation.UserNotExists;

@Service
public class UserService implements UseCase {

    @Autowired
    private FindUserByUsername searcher;

    @Autowired
    private SaveUser saver;

    @Autowired
    private FindAll finder;

    public User findUserByUsername(String username) throws UsernameNotFoundException {
	User user = searcher.findByUsername(username);
	UserNotExists.validate(user, username);
	return user;
    }

    public User createUser(User user) throws UserExistsException {
	User existent = searcher.findByUsername(user.getUsername());
	UserExists.validate(existent);
	user.setPassword(Encoder.passwordEncoder().encode(user.getPassword()));
	user.addRole(User.UserRole.CLIENT);
	saver.save(user);
	return user;
    }

    public List<User> findAll() {
	return finder.findAll();
    }

}
