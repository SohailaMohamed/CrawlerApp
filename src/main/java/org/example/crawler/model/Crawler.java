package org.example.crawler.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "CRAWLER")
@Entity
public class Crawler {

    @Id
    @SequenceGenerator(
            name = "crawler_seq",
            sequenceName = "crawler_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "crawler_seq"
    )
    @Column(name = "ID", updatable = false)
    private long id;

    @Column(name = "URL", nullable = false, columnDefinition = "TEXT")
    private String url;

    public Crawler() {
    }

    public Crawler(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Crawler{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
