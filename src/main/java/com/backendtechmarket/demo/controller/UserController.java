package com.backendtechmarket.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backendtechmarket.demo.dto.ResponseDto;
import com.backendtechmarket.demo.dto.user.SignInDto;
import com.backendtechmarket.demo.dto.user.SignInResponseDto;
import com.backendtechmarket.demo.dto.user.SignUpDto;
import com.backendtechmarket.demo.entity.User;
import com.backendtechmarket.demo.exeptions.AuthenticationFailException;
import com.backendtechmarket.demo.exeptions.CustomException;
import com.backendtechmarket.demo.repository.UserRepository;
import com.backendtechmarket.demo.services.AuthenticationService;
import com.backendtechmarket.demo.services.UserService;

@RequestMapping("user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public User findUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        user.getEmail();
        return userRepository.findUserByEmail(user.getEmail());
    }

    @PostMapping("/signup")
    public ResponseDto Signup(@RequestBody SignUpDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }

    // TODO token should be updated
    @PostMapping("/signIn")
    public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws CustomException {
        return userService.signIn(signInDto);
    }
}
