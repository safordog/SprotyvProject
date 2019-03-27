package com.gmail.safordog.newsreport.model;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name="articles")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String source;
    @Column(length = 1024)
    private String link;
    @Column(length = 512)
    private String title;
    private java.sql.Timestamp pubDate;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "insert_timestamp", nullable = false)
    private java.util.Date insertTimestamp;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "article")
    private transient ArticleTextAndTags articleTextAndTags;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private transient Author author;
    @OneToMany(targetEntity = com.gmail.safordog.newsreport.model.Comment.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id")
    private transient List<Comment> comments = new ArrayList<>();

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Article() {
    }

    public void setLink(String link) {
        this.link = link;
    }



    public Article(String source, String link, java.sql.Timestamp pubDate) {
        this.source = source;
        this.link = link;
        this.pubDate = pubDate;
    }

    public Article(String source, String link, String title, java.sql.Timestamp pubDate) {
        this.source = source;
        this.link = link;
        this.title = title;
        this.pubDate = pubDate;
    }

    public long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getLink() {
        return link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setArticle(String link) {
        this.link = link;
    }

    public void setPubDate(java.sql.Timestamp pubDate) {
        this.pubDate = pubDate;
    }

    public ArticleTextAndTags getArticleTextAndTags() {
        return articleTextAndTags;
    }

    public void setArticleTextAndTags(ArticleTextAndTags articleTextAndTags) {
        this.articleTextAndTags = articleTextAndTags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comment) {
        this.comments = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getInsertTimestamp() {
        return insertTimestamp;
    }

    public void setInsertTimestamp(Date insertTimestamp) {
        this.insertTimestamp = insertTimestamp;
    }

    @PrePersist
    protected void onCreate() {
        insertTimestamp = new Date();
    }

    public java.sql.Timestamp stringToTimeStampFormatter(String dateStr) {
//        String input = "Thu, 03 Mar 2016 15:30:00 +0200";
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME ;
        OffsetDateTime odt = OffsetDateTime.parse( dateStr, formatter );
        Instant instant = odt.toInstant();
        java.sql.Timestamp ts = java.sql.Timestamp.from(instant);
        return ts;
    }
}
