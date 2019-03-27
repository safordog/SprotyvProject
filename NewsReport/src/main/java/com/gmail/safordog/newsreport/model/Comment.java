package com.gmail.safordog.newsreport.model;

import javax.persistence.*;

@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text", length = 2048)
    private String text;
    @Column(unique = true)
    private Long date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentator_id", nullable = false)
    private Commentator commentator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Comment() {
    }

    public Comment(String text, Long date) {
        this.text = text;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Commentator getCommentator() {
        return commentator;
    }

    public void setCommentator(Commentator commentator) {
        this.commentator = commentator;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
