package com.example.facebook_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="type")
    String type;

    @Column(name="message")
    String message;

    @ManyToOne
    @JoinColumn(name = "sender_id" ,nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id",nullable = false)
    private User receiver;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;
}
