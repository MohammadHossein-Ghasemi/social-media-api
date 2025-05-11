package com.muhu.SocialMediaApi.controller;

import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.model.CommentDto;
import com.muhu.SocialMediaApi.model.CommentRegistrationDto;
import com.muhu.SocialMediaApi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<?> saveComment(@RequestBody CommentRegistrationDto commentRegistrationDto){
        CommentDto savedComment = commentService.saveComment(commentRegistrationDto);
        if (null == savedComment){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/comment/save")
                    .body("There is problem on save comment");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "status","success",
                        "message","Comment saved successfully.",
                        "data",savedComment
                ));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long commentId){
        Boolean isCommentDelete = commentService.deleteCommentById(commentId);
        if (!isCommentDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/comment/delete/"+commentId)
                    .body("There is problem on deleting comment.");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/comment/delete/"+commentId)
                .body("The comment with ID "+ commentId +" was deleted.");

    }

    @DeleteMapping("/delete/user-email")
    public ResponseEntity<?> deleteCommentByPostId(@RequestParam String userEmail){
        Boolean isCommentDelete = commentService.deleteCommentByUserEmail(userEmail);
        if (!isCommentDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/comment/delete/user-email")
                    .body("There is problem on deleting comment.");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/comment/delete/user-email")
                .body("All comments with user Email "+ userEmail +" were deleted.");
    }

    @DeleteMapping("/delete/post-id")
    public ResponseEntity<?> deleteCommentByPostId(@RequestParam Long postId){
        Boolean isCommentDelete = commentService.deleteCommentByPostId(postId);
        if (!isCommentDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/comment/delete/post-id")
                    .body("There is problem on deleting comment");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/comment/delete/post-id")
                .body("All comments with post ID "+ postId +" were deleted.");
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                        @RequestBody Comment comment){

        CommentDto updateComment = commentService.updateComment(commentId,comment);
        if (null == updateComment){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/comment/update/"+commentId)
                    .body("There is problem on updating comment");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/comment/update/"+commentId)
                .body(Map.of(
                        "status","success",
                        "message","Comment updated successfully.",
                        "data",updateComment
                ));

    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllComment(@RequestParam(required = false) Integer page ,
                                           @RequestParam(required = false) Integer size){
        Page<CommentDto> allComment = commentService.getAllComment(page, size);
        HttpStatus httpStatus = allComment.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/comment/all")
                .body(allComment);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable Long commentId){
        CommentDto foundedComment = commentService.getCommentById(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/comment/"+commentId)
                .body(Map.of(
                        "status","success",
                        "message","Comment founded successfully.",
                        "data",foundedComment
                ));
    }

    @GetMapping("/user-id")
    public ResponseEntity<?> getAllCommentByUserId(@RequestParam Long userId,
                                                   @RequestParam(required = false) Integer page ,
                                                   @RequestParam(required = false) Integer size){
        Page<CommentDto> allCommentByUserId = commentService.getAllCommentByUserId(userId,page,size);

        HttpStatus httpStatus = allCommentByUserId.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/comment/user-id")
                .body(allCommentByUserId);
    }

    @GetMapping("/user-email")
    public ResponseEntity<?> getAllCommentByUserEmail(@RequestParam String userEmail,
                                                      @RequestParam(required = false) Integer page ,
                                                      @RequestParam(required = false) Integer size){
        Page<CommentDto> allCommentByUserEmail = commentService.getAllCommentByUserEmail(userEmail,page,size);

        HttpStatus httpStatus = allCommentByUserEmail.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/comment/user-email")
                .body(allCommentByUserEmail);
    }

    @GetMapping("/post-id")
    public ResponseEntity<?> getAllCommentByPostId(@RequestParam Long postId,
                                                   @RequestParam(required = false) Integer page ,
                                                   @RequestParam(required = false) Integer size){
        Page<CommentDto> allCommentByUserId = commentService.getAllCommentByPostId(postId,page,size);

        HttpStatus httpStatus = allCommentByUserId.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/comment/post-id")
                .body(allCommentByUserId);
    }
}
