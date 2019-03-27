package com.gmail.safordog.newsreport.model;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "authors")
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 512)
    private String name;
    @OneToMany(targetEntity = com.gmail.safordog.newsreport.model.Article.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private transient Set<Article> articles = new HashSet<>();

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public Author(String name, Set<Article> articles) {
        this.name = name;
        this.articles = articles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public String getPubAuthor(Document doc, String sourceLink) {
        String authorParse = null;
        Elements authorEl = null;
        switch (sourceLink) {
            case "https://24tv.ua/": {
                authorEl = doc.getElementsByClass("authors");
                if (authorEl.size() == 0) {
                    authorParse = "unknown";
                } else {
                    authorParse = authorEl.get(0).text();
                }
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if(authorEl == null) {
                    authorParse = "unknown";
                }
                break;
            }
            case "https://www.segodnya.ua": {
                authorEl = doc.select(".authors p");
                if (authorEl.size() == 0) {
                    authorParse = "unknown";
                } else {
                    authorParse = authorEl.get(0).text();
                }
                break;
            }
            case "https://strana.ua": {
                authorEl = doc.getElementsByAttributeValue("itemprop", "author");
                if (authorEl.size() == 0) {
                    authorParse = "unknown";
                } else {
                    authorParse = authorEl.get(0).text();
                }
                break;
            }
            case "https://apostrophe.ua": {
                authorEl = doc.select(".article__author a");
                if (authorEl.size() == 0) {
                    authorParse = "unknown";
                } else {
                    authorParse = authorEl.get(0).text();
                }
                break;
            }
            default: {
                authorEl = doc.getElementsByClass("authors");
                if (authorEl.size() == 0) {
                    authorParse = "unknown";
                } else {
                    authorParse = authorEl.get(0).text();
                }
            }
        }
        return authorParse;
    }
}
