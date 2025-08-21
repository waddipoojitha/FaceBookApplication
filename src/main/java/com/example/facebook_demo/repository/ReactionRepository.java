package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction,Integer>{

    List<Reaction> findByParentIdAndParentTypeAndDeletedAtIsNull(int parentId, String parentType);

    List<Reaction> findByDeletedAtIsNull();

    Optional<Reaction> findByIdAndDeletedAtIsNull(int id);

    boolean existsByUserIdAndParentIdAndParentTypeAndDeletedAtIsNull(int id, int parentId, String parentType);

}
