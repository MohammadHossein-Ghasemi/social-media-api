package com.muhu.SocialMediaApi.controller;


import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.model.LikeDto;
import com.muhu.SocialMediaApi.model.LikeRegistrationDto;
import com.muhu.SocialMediaApi.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/save")
    public ResponseEntity<?> saveLike(@RequestBody LikeRegistrationDto likeRegistrationDto){
        LikeDto savedLike = likeService.saveLike(likeRegistrationDto);
        if (null == savedLike){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/like/save")
                    .body("There is problem on save like");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION,"/api/like/save")
                .body(Map.of(
                        "status","success",
                        "message","Like saved successfully.",
                        "data",savedLike
                ));
    }

    @DeleteMapping("/delete/{likeId}")
    public ResponseEntity<?> deleteLikeById(@PathVariable Long likeId){
        Boolean isLikeDelete = likeService.deleteLikeById(likeId);
        if (!isLikeDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/like/delete/"+likeId)
                    .body("There is problem on deleting like");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/like/delete/"+likeId)
                .body("The like with ID "+likeId+" was deleted.");
    }

    @PutMapping("/update/{likeId}")
    public ResponseEntity<?> updateLike(@PathVariable Long likeId,
                                        @RequestBody Like like){

        LikeDto updatedLike = likeService.updateLike(likeId,like);
        if (null == updatedLike){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/like/update/"+likeId)
                    .body("There is problem on updating like");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/like/update/"+likeId)
                .body(Map.of(
                        "status","success",
                        "message","Like updated successfully.",
                        "data",updatedLike
                ));

    }

    @GetMapping("/{likeId}")
    public ResponseEntity<?> getLikeByID(@PathVariable Long likeId){
        LikeDto foundedLike = likeService.getLikeByID(likeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/like/"+likeId)
                .body(Map.of(
                        "status","success",
                        "message","Like founded successfully.",
                        "data",foundedLike
                ));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLike(@RequestParam(required = false) Integer page ,
                                        @RequestParam(required = false) Integer size){
        Page<LikeDto> allLike = likeService.getAllLike(page, size);

        HttpStatus httpStatus = allLike.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/like/all")
                .body(allLike);
    }

    @GetMapping("/user-id")
    public ResponseEntity<?> getAllLikeByUserId(@RequestParam Long userId,
                                                @RequestParam(required = false) Integer page ,
                                                @RequestParam(required = false) Integer size){
        Page<LikeDto> allLikeByUserId = likeService.getAllLikeByUserId(userId,page,size);

        HttpStatus httpStatus = allLikeByUserId.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/like/user-id")
                .body(allLikeByUserId);
    }

    @GetMapping("/user-email")
    public ResponseEntity<?> getAllLikeByUserEmail(@RequestParam String userEmail,
                                                   @RequestParam(required = false) Integer page ,
                                                   @RequestParam(required = false) Integer size){
        Page<LikeDto> allLikeByUserEmail = likeService.getAllLikeByUserEmail(userEmail,page,size);

        HttpStatus httpStatus = allLikeByUserEmail.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/like/user-email")
                .body(allLikeByUserEmail);
    }

    @GetMapping("/post-id")
    public ResponseEntity<?> getAllLikeByPostId(@RequestParam Long postId,
                                                @RequestParam(required = false) Integer page ,
                                                @RequestParam(required = false) Integer size){
        Page<LikeDto> allLikeByPostId = likeService.getAllLikeByPostId(postId,page,size);

        HttpStatus httpStatus = allLikeByPostId.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/like/post-id")
                .body(allLikeByPostId);
    }

}
