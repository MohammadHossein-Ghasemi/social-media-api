package com.muhu.SocialMediaApi.controller;

import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.mapper.UserMapper;
import com.muhu.SocialMediaApi.model.UserDto;
import com.muhu.SocialMediaApi.model.UserRegistrationDto;
import com.muhu.SocialMediaApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.muhu.SocialMediaApi.mapper.UserMapper.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@RequestBody UserRegistrationDto userRegistrationDto){
        UserDto savedUser = userToUserDto(userService.saveUser(userRegistrationDtoToUser(userRegistrationDto)));
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
        UserDto updatedUser = userToUserDto(userService.updateUser(userEmail, user)) ;
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
    private ResponseEntity<?> getAllUser(){
        List<UserDto> allUser = userService.getAllUser().stream()
                .map(UserMapper::userToUserDto)
                .toList();
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
        UserDto foundedUser = userToUserDto(userService.getUserById(userId));
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
        UserDto foundedUser = userToUserDto(userService.getUserByEmail(userEmail));
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
    public ResponseEntity<?> getAllUserFollowersById(@PathVariable Long userId){
        List<UserDto> allUserFollowersById = userService.getAllUserFollowersById(userId)
                .stream()
                .map(UserMapper::userToUserDto)
                .toList();
        HttpStatus status = allUserFollowersById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/followers")
                .body(allUserFollowersById);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<?> getAllUserFollowingById(@PathVariable Long userId){
        List<UserDto> allUserFollowingById = userService.getAllUserFollowingById(userId)
                .stream()
                .map(UserMapper::userToUserDto)
                .toList();
        HttpStatus status = allUserFollowingById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/following")
                .body(allUserFollowingById.isEmpty() ? "There is no user to display." : allUserFollowingById );
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getAllUserFollowersByEmail(@RequestParam String userEmail){
        List<UserDto> allUserFollowersById = userService.getAllUserFollowersByEmail(userEmail)
                .stream()
                .map(UserMapper::userToUserDto)
                .toList();
        HttpStatus status = allUserFollowersById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/followers")
                .body(allUserFollowersById);
    }

    @GetMapping("/following")
    public ResponseEntity<?> getAllUserFollowingByEmail(@RequestParam String userEmail){
        List<UserDto> allUserFollowingById = userService.getAllUserFollowingByEmail(userEmail)
                .stream()
                .map(UserMapper::userToUserDto)
                .toList();
        HttpStatus status = allUserFollowingById.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.LOCATION,"/api/user/following")
                .body(allUserFollowingById.isEmpty() ? "There is no user to display." : allUserFollowingById );
    }
}
