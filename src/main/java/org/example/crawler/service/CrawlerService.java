package org.example.crawler.service;

import org.example.crawler.model.Crawler;
import org.example.crawler.repo.CrawlerRepository;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class CrawlerService {

    CrawlerRepository crawlerRepository;
    private Set<String> urlLink;
    private List<Crawler> crawlerList;

    @Autowired
    public CrawlerService(CrawlerRepository crawlerRepository, Set<String> urlLink, List<Crawler> crawlerList) {
        this.crawlerRepository = crawlerRepository;
        this.urlLink = urlLink;
        this.crawlerList = crawlerList;
    }

    public List<Crawler> getCrawlerList() {
        return crawlerList;
    }

    public List<String> getAllCrawler(){
        List<String> urlS = new ArrayList<>();
        List<Crawler> crawlers = crawlerRepository.findAll();
        for (Crawler crawler: crawlers) {
            urlS.add(crawler.getUrl());
        }
        return urlS;
    }

    public void setAllCrawler(List<Crawler> crawlers){
        if(!crawlers.isEmpty()){
            crawlerRepository.saveAllAndFlush(crawlers);
        }
    }

    private static final Pattern FILE_ENDING_EXCLUSION_PATTERN = Pattern.compile(
            ".*(\\.(" +
            ".*\\." +
            ".*\\" +
            ".*//." +
            ".*//" +
            "css|js" +
            "|bmp|gif|jpe?g|JPE?G|png|tiff?|ico|nef|raw" +
            "|mid|mp2|mp3|mp4|wav|wma|flv|mpe?g" +
            "|avi|mov|mpeg|ram|m4v|wmv|rm|smil" +
            "|pdf|doc|docx|pub|xls|xlsx|vsd|ppt|pptx" +
            "|swf" +
            "|zip|rar|gz|bz2|7z|bin" +
            "|xml|txt|java|c|cpp|exe" +
            "))$"
    );

    private static final Pattern INCLUDED_PATTERN = Pattern.compile(
            "^(https)://.*$"+
            "*(monzo.com).*$"
    );
    public boolean shouldVisit(String url) {
         if(!FILE_ENDING_EXCLUSION_PATTERN.matcher(url.toLowerCase()).matches()){
             return INCLUDED_PATTERN.matcher(url.toLowerCase()).matches();
         }
         return false;
    }
    public void getPageLinks(String url) {
        Validate.isTrue(url.length()>1, url);
        if (shouldVisit(url)) {
            try {
                if (urlLink.add(url)) {
                    System.out.println(url);
                    crawlerList.add(new Crawler(url));
                }
                Document doc = Jsoup.connect(url).get();
                Elements availableLinksOnPage = doc.select("a[href]");
                for (Element ele : availableLinksOnPage) {
                    Thread crawler = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getPageLinks(ele.attr("abs:href"));
                        }
                    });
                    crawler.start();
                }
            }
            catch (IOException e) {
                System.err.println("For '" + url + "': " + e.getMessage());
            }
        }
    }

}
