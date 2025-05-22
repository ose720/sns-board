package com.example.snsboard.service;

import com.example.snsboard.exception.post.PostNotFoundException;
import com.example.snsboard.exception.user.UserNotAllowedException;
import com.example.snsboard.exception.user.UserNotFoundException;
import com.example.snsboard.model.entity.LikeEntity;
import com.example.snsboard.model.entity.PostEntity;
import com.example.snsboard.model.entity.UserEntity;
import com.example.snsboard.model.post.Post;
import com.example.snsboard.model.post.PostPatchRequestBody;
import com.example.snsboard.model.post.PostPostRequestBody;
import com.example.snsboard.repository.LikeEntityRepository;
import com.example.snsboard.repository.PostEntityRepository;
import com.example.snsboard.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

  @Autowired private PostEntityRepository postEntityRepository;
  @Autowired private UserEntityRepository userEntityRepository;

  @Autowired private LikeEntityRepository likeEntityRepository;

  public List<Post> getPosts(UserEntity currentUser) {
    var projections = postEntityRepository.findPostsWithLikingStatus(currentUser.getUserId());
    return projections.stream().map(Post::from).toList();
  }

  public List<Post> getPostsByUsername(String username, UserEntity currentUser) {
    var user =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    var projections =
        postEntityRepository.findPostsByUserIdWithLikingStatus(
            user.getUserId(), currentUser.getUserId());
    return projections.stream().map(Post::from).toList();
  }

  public Post getPostByPostId(Long postId, UserEntity currentUser) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    return getPostWithLikingStatus(postEntity, currentUser);
  }

  public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {
    var savedPostEntity =
        postEntityRepository.save(PostEntity.of(postPostRequestBody.body(), currentUser));
    return Post.from(savedPostEntity);
  }

  public Post updatePost(
      Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

    if (!postEntity.getUser().equals(currentUser)) {
      throw new UserNotAllowedException();
    }

    postEntity.setBody(postPatchRequestBody.body());
    var updatedEntity = postEntityRepository.save(postEntity);
    return Post.from(updatedEntity);
  }

  public void deletePost(Long postId, UserEntity currentUser) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

    if (!postEntity.getUser().equals(currentUser)) {
      throw new UserNotAllowedException();
    }

    postEntityRepository.delete(postEntity);
  }

  @Transactional
  public Post toggleLike(Long postId, UserEntity currentUser) {
    PostEntity postEntity =
        postEntityRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    var likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

    if (likeEntity.isPresent()) {
      likeEntityRepository.delete(likeEntity.get());
      postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
      return Post.from(postEntityRepository.save(postEntity), false);
    } else {
      likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
      postEntity.setLikesCount(postEntity.getLikesCount() + 1);
      return Post.from(postEntityRepository.save(postEntity), true);
    }
  }

  private Post getPostWithLikingStatus(PostEntity postEntity, UserEntity currentUser) {
    var isLiking = likeEntityRepository.findByUserAndPost(currentUser, postEntity).isPresent();
    return Post.from(postEntity, isLiking);
  }
}
