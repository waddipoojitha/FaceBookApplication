package com.example.facebook_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Integer>{

    List<Notification> findByReceiverId(int id);
} 
