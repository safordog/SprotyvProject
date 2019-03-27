package com.gmail.safordog.newsreport.model.sources;

import com.gmail.safordog.newsreport.model.Article;
import com.gmail.safordog.newsreport.model.Commentator;
import com.gmail.safordog.newsreport.repository.parser.ArticleRepository;
import com.gmail.safordog.newsreport.repository.parser.CommentRepository;
import com.gmail.safordog.newsreport.repository.parser.CommentatorRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface SourceInterface {

    public void parseCommentsAndCommentators(ArticleRepository articleRepository, WebDriver driver, CommentRepository commentRepository,
                                             CommentatorRepository commentatorRepository, Article article);

    public List<Long> parseElToLong(List<WebElement> timeListEl);

    public void saveCommentatorAndComments(CommentRepository commentRepository, ArticleRepository articleRepository,
                                           CommentatorRepository commentatorRepository, Article article,
                                           List<WebElement> commentsList, List<Long> timeListLong,
                                           List<WebElement> commentatorsList);

    public Commentator getActualCommentator(String account, String name, CommentatorRepository commentatorRepository);
}
