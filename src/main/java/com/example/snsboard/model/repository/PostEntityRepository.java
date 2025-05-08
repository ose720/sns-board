package com.example.snsboard.model.repository;

import com.example.snsboard.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  PostEntityRepository extends JpaRepository<PostEntity, Long> {

}
