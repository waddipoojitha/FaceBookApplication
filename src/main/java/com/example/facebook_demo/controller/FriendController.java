package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.FriendService;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired private FriendService friendService;

    @PostMapping("/send-request/{receiverId}")
    public ResponseEntity<APIResponse<String>> sendRequest(@PathVariable int receiverId){
        String result=friendService.sendRequest(receiverId);
        APIResponse<String> apiResponse=new APIResponse<>("Requested to user id "+receiverId,result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/accept-request/{requestId}")
    public ResponseEntity<APIResponse<String>> acceptRequest(@PathVariable int requestId) {
        String result = friendService.respondToRequest(requestId,true);
        APIResponse<String> apiResponse=new APIResponse<>("Friend request accepted from "+requestId,result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/reject-request/{requestId}")
    public ResponseEntity<APIResponse<String>> rejectRequest(@PathVariable int requestId) {
        String result = friendService.respondToRequest(requestId, false);
        APIResponse<String> apiResponse=new APIResponse<>("Friend request rejected from "+requestId,result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<APIResponse<List<UserDTO>>> getPendingRequests() {
        List<UserDTO> pendingRequests=friendService.getPendingRequests();
        APIResponse<List<UserDTO>> apiResponse=new APIResponse<>("Pending requests fetched",pendingRequests);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserDTO>>> getFriends() {
        List<UserDTO> friends=friendService.getFriends();
        APIResponse<List<UserDTO>> apiResponse=new APIResponse<>("Your friends list fetched ",friends);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}