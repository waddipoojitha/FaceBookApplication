package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.FriendRequest;
import com.example.facebook_demo.entity.RequestStatus;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.FriendRequestException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.FriendRequestRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class FriendService {

    @Autowired private UserRepository userRepo;
    @Autowired private FriendRequestRepository friendRequestRepo;
    @Autowired private ModelMapper modelMapper;

    public String sendRequest(int receiverId) {
        String username=SecurityUtils.getCurrentUsername();
        User sender=userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(()->new ResourceNotFoundException("Sender not found"));
        User receiver=userRepo.findByIdAndDeletedAtIsNull(receiverId).orElseThrow(()->new ResourceNotFoundException("Receiver not found"));
        if(sender.getId()==(receiver.getId())){
            throw new FriendRequestException("You cannot send request to yourself");
        }

        if(friendRequestRepo.existsBySenderAndReceiverAndStatus(sender, receiver, RequestStatus.ACCEPTED)){
            throw new FriendRequestException("You are already friends");
        }
        if(friendRequestRepo.existsBySenderAndReceiverAndStatus(receiver, sender, RequestStatus.ACCEPTED)){
            throw new FriendRequestException("You are already friends");
        }

        if(friendRequestRepo.existsBySenderAndReceiverAndStatus(sender,receiver,RequestStatus.PENDING)){
            throw new FriendRequestException("Friend request already in pending");
        }
        if(friendRequestRepo.existsBySenderAndReceiverAndStatus(receiver,sender,RequestStatus.PENDING)){
            throw new FriendRequestException("Friend request already in pending");
        }

        Optional<FriendRequest> rejectedRequest = friendRequestRepo.findBySenderAndReceiverAndStatus(sender, receiver, RequestStatus.REJECTED);

        if (rejectedRequest.isPresent()) {
            FriendRequest request = rejectedRequest.get();
            request.setStatus(RequestStatus.PENDING);
            request.setRequestedAt(LocalDateTime.now());
            friendRequestRepo.save(request);
            return "Friend request re-sent";
        }
        
        FriendRequest request=new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());

        friendRequestRepo.save(request);
        return "Friend request sent";
    }

    public String respondToRequest(int requestId, boolean accept) {
        String username=SecurityUtils.getCurrentUsername();
        FriendRequest request=friendRequestRepo.findById(requestId).orElseThrow(()->new ResourceNotFoundException("Request not found"));

        if(!request.getReceiver().getUsername().equals(username)){throw new FriendRequestException("You cannot respond to someone else friend request");}
        if (request.getStatus() != RequestStatus.PENDING) {throw new FriendRequestException("Request already processed");}

        request.setStatus(accept? RequestStatus.ACCEPTED:RequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());
        friendRequestRepo.save(request);

        return accept?"Friend request accepted":"Friend request rejected";
    }

    public List<UserDTO> getPendingRequests() {
        String username=SecurityUtils.getCurrentUsername();
        User receiver=userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return friendRequestRepo.findByReceiverAndStatus(receiver,RequestStatus.PENDING).stream()
                                .map(FriendRequest::getSender)
                                .map(user->modelMapper.map(user,UserDTO.class))
                                .collect(Collectors.toList());
    }

    public List<UserDTO> getFriends() {
        String username=SecurityUtils.getCurrentUsername();
        User user=userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
        List<User> received=friendRequestRepo.findByReceiverAndStatus(user, RequestStatus.ACCEPTED).stream().map(FriendRequest::getSender).toList();
        List<User> sent=friendRequestRepo.findBySenderAndStatus(user,RequestStatus.ACCEPTED).stream().map(FriendRequest::getReceiver).toList();
        List<User> friends=new ArrayList<>();
        friends.addAll(received);
        friends.addAll(sent);

        return friends.stream().map(friend->modelMapper.map(friend,UserDTO.class)).collect(Collectors.toList());
    }
}
