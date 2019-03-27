package com.gmail.safordog.newsreport.repository.parser;

import com.gmail.safordog.newsreport.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    boolean existsArticleByLink(String link);

    @Query("SELECT t.link FROM articles t")
    List<String> getAllLinks();

    Article findArticleByLink(String link);

    List<Article> findArticlesByInsertTimestampGreaterThan(Date date);

    Page<Article> findByInsertTimestamp(Date date, Pageable pageable);

    Page<Article> findTopBy(Pageable pageable);

    Article findArticlesById(Long id);






}
