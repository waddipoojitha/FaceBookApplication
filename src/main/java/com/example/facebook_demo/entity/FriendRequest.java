package com.example.facebook_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="sender_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id",nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable = false)
    private RequestStatus status;

    @Column(name="requested_at",nullable = false)
    private LocalDateTime requestedAt;

    @Column(name="responded_at")
    private LocalDateTime respondedAt;
}


