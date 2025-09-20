package com.marmoraria.wiki.api.wiki_api.dto;

import com.marmoraria.wiki.api.wiki_api.domain.WikiPage;
import lombok.Data;

import java.time.OffsetTime;

@Data
public class WikiPageDTO {
    private Integer id;
    private String key;
    private String title;
    private String content;
    private OffsetTime created_at;
    private OffsetTime updated_at;

    public WikiPageDTO(WikiPage wikiPage) {
        this.id = wikiPage.getId();
        this.key = wikiPage.getKey();
        this.title = wikiPage.getTitle();
        this.content = wikiPage.getContent();
        this.created_at = wikiPage.getCreated_at();
        this.updated_at = wikiPage.getUpdated_at();

    }
}
