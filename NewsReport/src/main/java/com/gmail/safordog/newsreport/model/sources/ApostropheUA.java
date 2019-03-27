package com.gmail.safordog.newsreport.model.sources;

import com.gmail.safordog.newsreport.model.*;
import com.gmail.safordog.newsreport.repository.parser.ArticleRepository;
import com.gmail.safordog.newsreport.repository.parser.CommentRepository;
import com.gmail.safordog.newsreport.repository.parser.CommentatorRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApostropheUA implements SourceInterface {

    private Logger logger;

    public ApostropheUA() {
    }

    public void parseCommentsAndCommentators(ArticleRepository articleRepository, WebDriver driver, CommentRepository commentRepository,
                                             CommentatorRepository commentatorRepository, Article article) {
        String commentatorName = null;
        String commentatorAccount = null;
            driver.get(article.getLink());
            List<WebElement> comments = (List<WebElement>) ((ChromeDriver) driver).findElementsByCssSelector("iframe");
            String url = "";
            for (WebElement el : comments) {
                if (el.getAttribute("src").startsWith("https://www.facebook.com/v3.2/plugins/comments")) {
                    url = el.getAttribute("src");
                    break;
                }
            }
        try {
            driver.get(url);
        } catch (Exception e) {
            System.out.println("Problem with comments url: " + e);
            logger = new Logger();
            try {
                logger.writeLog("Problem with comments url: " + e);
            } catch (IOException e1) {
                System.out.println(e1);
            }
        }
        WebElement body = driver.findElement(By.tagName("body"));
            List<WebElement> commentsList = body.findElements(By.cssSelector("._5mdd"));
            List<WebElement> timeListEl = body.findElements(By.cssSelector("abbr"));
            List<Long> timeListLong = parseElToLong(timeListEl);
            List<WebElement> commentatorsList = body.findElements(By.cssSelector(" .UFICommentActorName"));

            System.out.println("comment: " + commentsList.size());
            System.out.println("time: " + timeListEl.size());
            System.out.println("commentator: " + commentatorsList.size());

            commentsList.stream().forEach(temp -> {
                System.out.println(temp.getText());
            });
            System.out.println("------------------");
            timeListEl.stream().forEach(temp -> {
                System.out.println(temp.getAttribute("data-utime"));
            });
            System.out.println("-------------------");
            commentatorsList.stream().forEach(temp -> {
                System.out.println(temp.getText());
            });

            saveCommentatorAndComments(commentRepository, articleRepository, commentatorRepository,
                    article, commentsList, timeListLong, commentatorsList);
           System.out.println(article.getLink());
    }

    public List<Long> parseElToLong(List<WebElement> timeListEl) {
        List<Long> list = new ArrayList<>();
        timeListEl.stream().forEach(el -> {
            list.add(Long.parseLong(el.getAttribute("data-utime")));
        });
        return list;
    }

    public void saveCommentatorAndComments(CommentRepository commentRepository, ArticleRepository articleRepository,
                                           CommentatorRepository commentatorRepository, Article article,
                                           List<WebElement> commentsList, List<Long> timeListLong,
                                           List<WebElement> commentatorsList) {

        Long date = null;
        for (int j = 0; j < commentsList.size(); j++) {
            Commentator commentator = null;
            Article articleVar = null;
            Comment comment = null;
            date = timeListLong.get(j);
            if (!commentRepository.existsCommentByDate(date)) {
                commentator =
                        getActualCommentator(commentatorsList.get(j)
                                .getAttribute("href"), commentatorsList.get(j).getText(), commentatorRepository);

                comment = new Comment(commentsList.get(j).getText(), date);
                article.getComments().add(comment);
                comment.setArticle(article);
                comment.setCommentator(commentator);
                try {
                    commentator.getComments().add(comment);
                    commentatorRepository.save(commentator);
                    commentRepository.save(comment);
                } catch (Exception e) {
                    logger = new Logger();
                    try {
                        logger.writeLog(e.toString());
                    } catch (IOException e1) {
                        System.out.println(e1);
                    }
                }
            }
        }
    }

    public Commentator getActualCommentator(String account, String name, CommentatorRepository commentatorRepository) {
        Commentator commentator = null;
        if (!commentatorRepository.existsCommentatorByAccount(account)
                || !commentatorRepository.existsCommentatorByName(name) && account == null) {
            commentator = new Commentator(name, account);
        } else {
            commentator = commentatorRepository.findCommentatorByAccountAndName(account, name);
        }
        return commentator;
    }
}
