package org.demo.security.auth;

import org.demo.security.auth.dto.AuthJwtResponse;
import org.demo.security.auth.dto.AuthLoginRequest;
import org.demo.security.auth.dto.AuthRegisterRequest;
import org.demo.security.config.JwtService;
import org.demo.security.exceptions.UserAlreadyExistException;
import org.demo.security.user.Role;
import org.demo.security.user.User;
import org.demo.security.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    final UserRepository userRepository;
    final JwtService jwtService;
    final PasswordEncoder passwordEncoder;
    final ModelMapper modelMapper;

    @Autowired
    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    public AuthJwtResponse login(AuthLoginRequest req) {
        Optional<User> user = userRepository.findByEmail(req.getEmail());

        if (user.isEmpty()) throw new UsernameNotFoundException("Email is not registered");

        if (!passwordEncoder.matches(req.getPassword(), user.get().getPassword()))
            throw new IllegalArgumentException("Password is incorrect");

        AuthJwtResponse response = new AuthJwtResponse();

        response.setToken(jwtService.generateToken(user.get()));
        return response;
    }

    public AuthJwtResponse register(AuthRegisterRequest req) {
        Optional<User> user = userRepository.findByEmail(req.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("Email is already registered");

        User newUser = modelMapper.map(req, User.class);
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());

        if(req.getIsAdmin()) newUser.setRole(Role.ADMIN);
        else newUser.setRole(Role.USER);

        userRepository.save(newUser);

        AuthJwtResponse response = new AuthJwtResponse();
        response.setToken(jwtService.generateToken(newUser));
        return response;
    }

}
