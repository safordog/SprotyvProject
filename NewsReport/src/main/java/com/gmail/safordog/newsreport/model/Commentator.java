package com.gmail.safordog.newsreport.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "commentators")
public class Commentator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "account", length = 1024)
    private String account;
    @OneToMany(targetEntity = com.gmail.safordog.newsreport.model.Comment.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "commentator_id")
    private List<Comment> comments = new ArrayList<>();

    public Commentator() {
    }

    public Commentator(String name, String account) {
        this.name = name;
        this.account = account;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> list) {
        this.comments = list;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
