package com.gmail.safordog.newsreport.repository.parser;

import com.gmail.safordog.newsreport.model.Commentator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentatorRepository extends JpaRepository<Commentator, Long> {

    boolean existsCommentatorByAccount(String account);
    boolean existsCommentatorByName(String name);

    Commentator findCommentatorByAccountAndName(String account, String name);
}
