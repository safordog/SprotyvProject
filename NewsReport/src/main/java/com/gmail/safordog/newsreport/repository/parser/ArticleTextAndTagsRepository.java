package com.gmail.safordog.newsreport.repository.parser;

import com.gmail.safordog.newsreport.model.ArticleTextAndTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTextAndTagsRepository extends JpaRepository<ArticleTextAndTags, Long> {

    List<ArticleTextAndTags> findArticleTextAndTagsByTagsContaining(String tag);
}
