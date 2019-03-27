package com.gmail.safordog.newsreport.controller;

import com.gmail.safordog.newsreport.model.Article;
import com.gmail.safordog.newsreport.model.ArticleTextAndTags;
import com.gmail.safordog.newsreport.model.DateAndCount;
import com.gmail.safordog.newsreport.repository.parser.ArticleRepository;
import com.gmail.safordog.newsreport.repository.parser.ArticleTextAndTagsRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleTextAndTagsRepository articleTextAndTagsRepository;

    @GetMapping("/getLastArticles")
    public void getLastArticles(HttpServletResponse resp) throws IOException {

        Pageable pageable = PageRequest.of(0, 30, Sort.Direction.DESC, "pubDate");
        Page<Article> lastArticlesPage = articleRepository.findAll(pageable);
        List<Article> lastArticlesList = lastArticlesPage.getContent();
        String jsonLastArticleList = new Gson().toJson(lastArticlesList);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF8");
        PrintWriter out = resp.getWriter();
        out.print(jsonLastArticleList);
        out.flush();
    }

    @GetMapping("/getSearchTagsArticles")
    public void getSearchTagsArticles(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<ArticleTextAndTags> listATAT = new ArrayList<>();
        List<Article> listArticles = new ArrayList<>();
        String[] arrTags = req.getParameter("tags").split(" ");

        for(String temp : arrTags) {
            listATAT.addAll(articleTextAndTagsRepository
                    .findArticleTextAndTagsByTagsContaining(temp));
        }

        listATAT.stream().forEach(temp -> {
            listArticles.add(articleRepository.findArticlesById(temp.getId()));
        });

        String jsonLastArticleList = new Gson().toJson(listArticles);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF8");
        resp.getWriter().write(jsonLastArticleList);
    }

    @GetMapping("/getCountAndDateArticles")
    public void getCountAndDateArticles(HttpServletResponse resp) throws IOException {

        Map<String, Long> respMap = new HashMap<>();
        int num = 0;
        List<DateAndCount> dacList = new ArrayList<>();
        List<String> listDate = new ArrayList<>();
        List<Article> allArticlesList = articleRepository.findAll(Sort.by(Sort.Direction.ASC, "pubDate"));
        allArticlesList.stream().forEach(temp -> {
            listDate.add((temp.getPubDate()).toString().substring(0, 10));
        });
        listDate.stream().forEach(temp -> {
            if(!respMap.containsKey(temp)) {
                Long count = listDate.stream().filter(str -> str.equals(temp)).count();
                respMap.put(temp, count);
                dacList.add(new DateAndCount(temp, count));
            }
        });
        String jsonLastArticleList = new Gson().toJson(dacList);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF8");
        resp.getWriter().write(jsonLastArticleList);
    }
}
