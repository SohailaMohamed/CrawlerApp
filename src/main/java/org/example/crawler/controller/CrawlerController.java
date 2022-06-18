package org.example.crawler.controller;

import org.example.crawler.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/v1/crawler")
public class CrawlerController {
    CrawlerService crawlerService;

    @Autowired
    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping
    public List<String> getCrawlerMap(@RequestParam("urlVariable") String urlVariable){

        crawlerService.getPageLinks(urlVariable);
        crawlerService.setAllCrawler(crawlerService.getCrawlerList());
        return crawlerService.getAllCrawler();

    }
}
