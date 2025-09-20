package com.marmoraria.wiki.api.wiki_api.dto;


import lombok.Data;

@Data
public class WikiPageRequestDTO {

    private String key;

    private String title;

    private String content;
}
