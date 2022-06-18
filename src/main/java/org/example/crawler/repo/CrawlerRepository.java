package org.example.crawler.repo;


import org.example.crawler.model.Crawler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlerRepository extends JpaRepository<Crawler, Long> {
}