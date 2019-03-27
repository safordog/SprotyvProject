package com.gmail.safordog.newsreport.model;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="text_tags")
public class ArticleTextAndTags implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;
    @Column(columnDefinition = "text")
    private String text;
    @Column(columnDefinition = "text")
    private String tags;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;


    public ArticleTextAndTags() {
    }

    public ArticleTextAndTags(String text, String tags) {
        this.text = text;
        this.tags = tags;
    }

    public ArticleTextAndTags(Long id, String text, String tags) {
        this.id = id;
        this.text = text;
        this.tags = tags;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getCorrectTags(String sourceLink, Document doc) {
        Elements tagsEl = null;
        String tags = null;
        switch (sourceLink) {
            case "https://24tv.ua/": {
                tagsEl = doc.select(".tags a");
                if(tagsEl.size() != 0) {
                    tags = tagsEl.text();
                }
                break;
            }
            case "https://apostrophe.ua": {
                tagsEl = doc.select(".row a");
                if(tagsEl.size() != 0) {
                    tags = tagsEl.text();
                }
                break;
            }
            case "https://www.segodnya.ua": {
                tagsEl = doc.getElementsByClass("tag");
                if(tagsEl.text().equals("")) {
                    tagsEl = doc.getElementsByClass("content-tags");
                }
                tags = tagsEl.text();
                break;
            }
            case "https://strana.ua": {
                tagsEl = doc.select(".tags a");
                tags = tagsEl.text();
                break;
            }
        }
        if(tags != null) {
            tags = tags.toLowerCase();
        }
        return tags;
    }

    public String getCorrectText(String sourceLink, Document doc) {
        Elements articlesEl = null;
        String text = null;
        switch (sourceLink) {
            case "https://24tv.ua/": {
                articlesEl = doc.select(".article_text");
//                if(articlesEl.size() > 0) {
//                    text = articlesEl.get(0).text();
//                }
                text = articlesEl.text();
                break;
            }
            case "https://apostrophe.ua": {
                articlesEl = doc.getElementsByAttributeValue("itemprop", "articleBody");
                text = articlesEl.text();
                break;
            }
            case "https://www.segodnya.ua": {
                articlesEl = doc.select(".content article p");
                if(articlesEl.text().equals("")) {
                    articlesEl = doc.select("p");
                }
                text = articlesEl.text();
                break;
            }
            case "https://strana.ua": {
                articlesEl = doc.select("#article-body");
                text = articlesEl.text();
                break;
            }
        }
        return text;
    }

}
