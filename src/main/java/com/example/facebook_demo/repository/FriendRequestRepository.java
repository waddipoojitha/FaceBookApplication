package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.FriendRequest;
import com.example.facebook_demo.entity.RequestStatus;
import com.example.facebook_demo.entity.User;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Integer>{

    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, RequestStatus pending);

    List<FriendRequest> findByReceiverAndStatus(User receiver, RequestStatus pending);

    List<FriendRequest> findBySenderAndStatus(User user, RequestStatus accepted);

    Optional<FriendRequest> findBySenderAndReceiverAndStatus(User sender, User receiver, RequestStatus rejected);
    
}
