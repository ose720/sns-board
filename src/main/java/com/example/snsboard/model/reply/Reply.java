package com.example.snsboard.model.reply;

import com.example.snsboard.model.entity.ReplyEntity;
import com.example.snsboard.model.post.Post;
import com.example.snsboard.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Reply(
    Long replyId,
    String body,
    User user,
    Post post,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    ZonedDateTime deletedDateTime) {
  public static Reply from(ReplyEntity replyEntity) {
    return new Reply(
        replyEntity.getReplyId(),
        replyEntity.getBody(),
        User.from(replyEntity.getUser()),
        Post.from(replyEntity.getPost()),
        replyEntity.getCreatedDateTime(),
        replyEntity.getUpdatedDateTime(),
        replyEntity.getDeletedDateTime());
  }
}
