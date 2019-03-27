package com.gmail.safordog.newsreport.repository.parser;

import com.gmail.safordog.newsreport.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    boolean existsCommentByDate(Long date);
}
