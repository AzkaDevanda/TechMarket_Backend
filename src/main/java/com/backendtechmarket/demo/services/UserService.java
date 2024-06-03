package com.backendtechmarket.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backendtechmarket.demo.config.MassageStrings;
import com.backendtechmarket.demo.dto.ResponseDto;
import com.backendtechmarket.demo.dto.user.SignInDto;
import com.backendtechmarket.demo.dto.user.SignInResponseDto;
import com.backendtechmarket.demo.dto.user.SignUpDto;
import com.backendtechmarket.demo.entity.AuthenticationToken;
import com.backendtechmarket.demo.entity.User;
import com.backendtechmarket.demo.enums.ResponseStatus;
import com.backendtechmarket.demo.enums.Role;
import com.backendtechmarket.demo.exeptions.AuthenticationFailException;
import com.backendtechmarket.demo.exeptions.CustomException;
import com.backendtechmarket.demo.repository.UserRepository;
import com.backendtechmarket.demo.utils.Helper;


@Service
public class UserService {

    public static final String USER_CREATED = "user created successfully";

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public ResponseDto signUp(SignUpDto signupDto)  throws CustomException {
        // Check to see if the current email address has already been registered.
        if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // If the email address has been registered then throw an exception.
            throw new CustomException("User already exists");
        }
        // first encrypt the password
        String encryptedPassword = signupDto.getPassword();
        // try {
        //     encryptedPassword = hashPassword(signupDto.getPassword());
        // } catch (NoSuchAlgorithmException e) {
        //     e.printStackTrace();
        //     logger.error("hashing password failed {}", e.getMessage());
        // }


        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), Role.user, encryptedPassword );

        User createdUser;
        try {
            // save the User
            createdUser = userRepository.save(user);
            // generate token for user
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            // save token in database
            authenticationService.saveConfirmationToken(authenticationToken);
            // success in creating
            return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

     public SignInResponseDto signIn(SignInDto signInDto) throws CustomException {
        // first find User by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if(!Helper.notNull(user)){
            throw  new AuthenticationFailException("user not present");
        }
        // check if password is right
        if (!user.getPassword().equals(signInDto.getPassword())){
            // passowrd doesnot match
            throw  new AuthenticationFailException(MassageStrings.WRONG_PASSWORD);
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if(!Helper.notNull(token)) {
            // token not present
            throw new CustomException("token not present");
        }

        return new SignInResponseDto ("success", token.getToken());
    }

}
