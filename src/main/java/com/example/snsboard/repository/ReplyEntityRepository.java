package com.example.snsboard.repository;

import com.example.snsboard.model.entity.PostEntity;
import com.example.snsboard.model.entity.ReplyEntity;
import com.example.snsboard.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Long> {
  List<ReplyEntity> findByUser(UserEntity user);

  List<ReplyEntity> findByPost(PostEntity post);
}
