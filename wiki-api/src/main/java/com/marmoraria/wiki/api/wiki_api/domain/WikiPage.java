package com.marmoraria.wiki.api.wiki_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetTime;

@Entity
@Table(name="wiki_page")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WikiPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "title")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at",    nullable = false, updatable = false)
    private OffsetTime created_at;
    @Column(name = "updated_at", nullable = false)
    private OffsetTime updated_at;

    @PrePersist
    protected void prePersist() {
        this.created_at = OffsetTime.now();
        this.updated_at = OffsetTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updated_at = OffsetTime.now();
    }
}
