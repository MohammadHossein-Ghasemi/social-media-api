package com.muhu.SocialMediaApi.controller;

import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.model.LoginRequestDto;
import com.muhu.SocialMediaApi.model.LoginResponseDto;
import com.muhu.SocialMediaApi.model.UserDto;
import com.muhu.SocialMediaApi.model.UserRegistrationDto;
import com.muhu.SocialMediaApi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@RequestBody UserRegistrationDto userRegistrationDto){
        String password = passwordEncoder.encode(userRegistrationDto.getPassword());
        userRegistrationDto.setPassword(password);
        UserDto savedUser = userService.saveUser(userRegistrationDto);
        if (null == savedUser){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/user/register")
                    .body("There is problem on register user");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION,"/api/user/register")
                .body(Map.of(
                        "status","success",
                        "message","User registered successfully.",
                        "data",savedUser
                ));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId){
        Boolean isUserDelete = userService.deleteUserById(userId);
        if (!isUserDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/user/delete")
                    .body("There is problem on deleting user");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/user/delete")
                .body("The user with ID "+userId+" was deleted.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByEmail(@RequestParam String userEmail){
        Boolean isUserDelete = userService.deleteUserByEmail(userEmail);
        if (!isUserDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/user/delete")
                    .body("There is problem on deleting user");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/user/delete")
                .body("The user with Email "+userEmail+" was deleted.");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam String userEmail,
                                        @RequestBody User user){
        UserDto updatedUser = userService.updateUser(userEmail, user) ;
        if (null == updatedUser){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/user/update")
                    .body("There is problem on updating user");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/user/update")
                .body(Map.of(
                        "status","success",
                        "message","User updated successfully.",
                        "data",updatedUser
                ));
    }

    @GetMapping("/all")
    private ResponseEntity<?> getAllUser(@RequestParam(required = false) Integer page ,
                                         @RequestParam(required = false) Integer size){
        Page<UserDto> allUser = userService.getAllUser(page,size);
        if (allUser.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .header(HttpHeaders.LOCATION,"/api/user/all")
                    .body("There is no user to display.");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/user/all")
                .body(allUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        UserDto foundedUser = userService.getUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/user/"+userId)
                .body(Map.of(
                        "status","success",
                        "message","User founded successfully.",
                        "data",foundedUser
                ));
    }

    @GetMapping()
    public ResponseEntity<?> getUserByEmail(@RequestParam String userEmail){
        UserDto foundedUser = userService.getUserByEmail(userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/user/")
                .body(Map.of(
                        "status","success",
                        "message","User founded successfully.",
                        "data",foundedUser
                ));
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getAllUserFollowersById(@PathVariable Long userId,
                                                     @RequestParam(required = false) Integer page ,
                                                     @RequestParam(required = false) Integer size){
        Page<UserDto> allUserFollowersById = userService.getAllUserFollowersById(userId,page,size);
        HttpStatus status = allUserFollowersById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/followers")
                .body(allUserFollowersById);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<?> getAllUserFollowingById(@PathVariable Long userId,
                                                     @RequestParam(required = false) Integer page ,
                                                     @RequestParam(required = false) Integer size){
        Page<UserDto> allUserFollowingById = userService.getAllUserFollowingById(userId,page,size);
        HttpStatus status = allUserFollowingById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/following")
                .body(allUserFollowingById.isEmpty() ? "There is no user to display." : allUserFollowingById );
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getAllUserFollowersByEmail(@RequestParam String userEmail,
                                                        @RequestParam(required = false) Integer page ,
                                                        @RequestParam(required = false) Integer size){
        Page<UserDto> allUserFollowersById = userService.getAllUserFollowersByEmail(userEmail,page,size);
        HttpStatus status = allUserFollowersById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/followers")
                .body(allUserFollowersById);
    }

    @GetMapping("/following")
    public ResponseEntity<?> getAllUserFollowingByEmail(@RequestParam String userEmail,
                                                        @RequestParam(required = false) Integer page ,
                                                        @RequestParam(required = false) Integer size){
        Page<UserDto> allUserFollowingById = userService.getAllUserFollowingByEmail(userEmail,page,size);
        HttpStatus status = allUserFollowingById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/following")
                .body(allUserFollowingById.isEmpty() ? "There is no user to display." : allUserFollowingById );
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginApi(@RequestBody LoginRequestDto loginRequestDto){
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDto.username(),
                loginRequestDto.password());
        Authentication authenticateResponse = authenticationManager.authenticate(authentication);
        if (null != authenticateResponse && authenticateResponse.isAuthenticated()){
            if (null != env){
                String secret = env.getProperty("JWT_SECRET","jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4");

                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                jwt= Jwts.builder()
                        .issuer("muhu")
                        .subject("Jwt Token")
                        .claim("username", authenticateResponse.getName())
                        .claim("authorities",authenticateResponse.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(".")))
                        .issuedAt(new java.util.Date())
                        .expiration(new java.util.Date(new java.util.Date().getTime()+3000000))
                        .signWith(secretKey).compact();
            }
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION,jwt)
                .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),jwt));
    }
}
