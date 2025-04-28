package com.muhu.SocialMediaApi.controller;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/save")
    public ResponseEntity<?> savePost(@RequestBody Post post){
        Post savePost = postService.savePost(post);
        if (null == savePost){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/post/save")
                    .body("There is problem on save post");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "status","success",
                        "message","Post saved successfully.",
                        "data",savePost
                ));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePostById(@PathVariable Long postId){
        Boolean isPostDelete = postService.deletePostById(postId);
        if (!isPostDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/post/delete")
                    .body("There is problem on deleting post");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/post/delete")
                .body("The post with ID "+postId+" was deleted.");

    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<?> updatePostById(@PathVariable Long postId,
                                            @RequestBody Post post){
        Post updatedPost = postService.updatePostById(postId, post);
        if (null == updatedPost){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/post/update")
                    .body("There is problem on updating post");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/post/update")
                .body(Map.of(
                        "status","success",
                        "message","Post updated successfully.",
                        "data",updatedPost
                ));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId){
        Post foundedPost = postService.getPostById(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/post/"+postId)
                .body(Map.of(
                        "status","success",
                        "message","Post founded successfully.",
                        "data",foundedPost
                ));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPost(){
        List<Post> allPost = postService.getAllPost();

        HttpStatus httpStatus = allPost.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/post/all")
                .body(allPost);
    }

    @GetMapping("/user-id")
    public ResponseEntity<?> getAllPostByUserId(@RequestParam Long userId){
        List<Post> allPostByUserId = postService.getAllPostByUserId(userId);

        HttpStatus httpStatus = allPostByUserId.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/post/user-id")
                .body(allPostByUserId);
    }

    @GetMapping("/user-email")
    public ResponseEntity<?> getAllPostByUserEmail(@RequestParam String userEmail){
        List<Post> allPostByUserEmail = postService.getAllPostByUserEmail(userEmail);

        HttpStatus httpStatus = allPostByUserEmail.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/post/user-email")
                .body(allPostByUserEmail);
    }
}
