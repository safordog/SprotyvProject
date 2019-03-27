package com.gmail.safordog.newsreport.parser;

import com.gmail.safordog.newsreport.model.Article;
import com.gmail.safordog.newsreport.model.ArticleTextAndTags;
import com.gmail.safordog.newsreport.model.Author;
import com.gmail.safordog.newsreport.model.Logger;
import com.gmail.safordog.newsreport.model.sources.ApostropheUA;
import com.gmail.safordog.newsreport.model.sources.SegodnyaUA;
import com.gmail.safordog.newsreport.model.sources.SourcesEnum;
import com.gmail.safordog.newsreport.model.sources.TwentyFour;
import com.gmail.safordog.newsreport.repository.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import io.github.bonigarcia.wdm.WebDriverManager;
import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Component
public class RunParser {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleTextAndTagsRepository articleTextAndTagsRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CommentatorRepository commentatorRepository;
    @Autowired
    private CommentRepository commentRepository;
    private WebDriver driver = getChromeDriver();
    private Logger logger = null;

    private final String[] SOURCES_ARRAY = {
            "https://24tv.ua/rss/tag/1792.xml",
            "https://apostrophe.ua/site/categoryfeed/?alias=politics",
            "https://apostrophe.ua/site/categoryfeed/?alias=economy",
            "https://apostrophe.ua/site/categoryfeed/?alias=society",
            "https://www.segodnya.ua/xml/rss",
            "https://strana.ua/xml/rss.xml"
    };


    @Scheduled(initialDelay = 3 * 1000, fixedDelay = 300 * 1000)
    public void runParserXML() {

        for(int i = 0; i < SOURCES_ARRAY.length; i++) {
            RSSFeedParser parser = new RSSFeedParser(SOURCES_ARRAY[i]);
            Feed feed = parser.readFeed();
            System.out.println("Parsing rss started " + feed);
            for (FeedMessage message : feed.getMessages()) {
                if (!articleRepository.existsArticleByLink(message.getLink()) || articleRepository.count() == 0) {
                    ArticleTextAndTags articleTextAndTags = parseTAT(message.getLink(), feed.getLink());
                    if(articleTextAndTags == null) {
                        continue;
                    }
                    Article article = new Article();
                    java.sql.Timestamp ts = article.stringToTimeStampFormatter(message.getPubDate());
                    article.setSource(feed.getLink());
                    article.setLink(message.getLink());
                    article.setTitle(message.getTitle());
                    article.setPubDate(ts);
                    String authorName = parseAuthor(message.getLink(), feed.getLink());
                    Author author = null;
                    if (!authorRepository.existsAuthorByName(authorName)) {
                        author = new Author(authorName);
                        authorRepository.save(author);
                        article.setAuthor(author);
                    } else {
                        author = authorRepository.findAuthorByName(authorName);
                        article.setAuthor(author);
                    }
                    article.setArticleTextAndTags(articleTextAndTags);
                    articleTextAndTags.setArticle(article);
                    author.getArticles().add(article);
                    articleRepository.save(article);
                    articleTextAndTagsRepository.save(articleTextAndTags);
                } else {
                    continue;
                }
            }
        }
            System.out.println("Parsing rss finished");
    }


    public ArticleTextAndTags parseTAT(String link, String sourceLink) {

        Document doc = null;
        String text = null;
        String tags = null;
        ArticleTextAndTags atat = new ArticleTextAndTags();

        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            logger = new Logger();
            try {
                logger.writeLog(e.toString());
            } catch (IOException e1) {
                System.out.println(e1);
            }
        }
        tags = atat.getCorrectTags(sourceLink, doc);
        text = atat.getCorrectText(sourceLink, doc);
        if(tags == null || text == null) {
            atat = null;
        } else {
            atat.setText(text);
            atat.setTags(tags);
        }
        return atat;
    }


    public String parseAuthor(String link, String sourceLink) {

        Author author = new Author();
        Document doc = null;
        String authorParse = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            logger = new Logger();
            try {
                logger.writeLog(e.toString());
            } catch (IOException e1) {
                System.out.println(e1);
            }
        }
        authorParse = author.getPubAuthor(doc, sourceLink);
        return authorParse;
    }


    public WebDriver getChromeDriver() {

        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36";
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.setHeadless(true);
	options.setBinary("/usr/bin/google-chrome-stable");
	options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriverManager.getInstance(CHROME).setup();
	//WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1000, 1800));
        return driver;
    }


    @Scheduled(initialDelay = 50 * 1000, fixedDelay = 5000 * 1000)
    public void runParseComments() {

        System.out.println("Parsing comments start");
        Date date = Date.from(ZonedDateTime.now().minusDays(20).toInstant());
        List<Article> articlesList = articleRepository.findArticlesByInsertTimestampGreaterThan(date);
        articlesList.stream().forEach(article -> {
            switch (article.getSource()) {
                case "https://24tv.ua/": {
                    SourcesEnum se = SourcesEnum.TWENTYFOUR;
                    Object obj = (TwentyFour) se.getSource();
                    ((TwentyFour) obj).parseCommentsAndCommentators(articleRepository, driver, commentRepository, commentatorRepository, article);
                    break;
                }
                case "https://apostrophe.ua": {
                    SourcesEnum se = SourcesEnum.APOSTROPHEUA;
                    Object obj = (ApostropheUA) se.getSource();
                    ((ApostropheUA) obj).parseCommentsAndCommentators(articleRepository, driver, commentRepository, commentatorRepository, article);
                    break;
                }
                case "https://www.segodnya.ua": {
                    SourcesEnum se = SourcesEnum.SEGODNYAUA;
                    Object obj = (SegodnyaUA) se.getSource();
                    ((SegodnyaUA) obj).parseCommentsAndCommentators(articleRepository, driver, commentRepository, commentatorRepository, article);
                    break;
                }

            }
        });
        System.out.println("Parsing comments finish");
    }

}


