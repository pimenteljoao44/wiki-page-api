package com.marmoraria.wiki.api.wiki_api.controllers;


import com.marmoraria.wiki.api.wiki_api.dto.WikiPageDTO;
import com.marmoraria.wiki.api.wiki_api.dto.WikiPageRequestDTO;
import com.marmoraria.wiki.api.wiki_api.services.WikiPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wiki-pages" )
public class WikiPageController {

    @Autowired
    private WikiPageService wikiPageService;

    @GetMapping
    public ResponseEntity<List<WikiPageDTO>> getAllWikiPages() {
        List<WikiPageDTO> pages = wikiPageService.findAll().stream()
                .map(WikiPageDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WikiPageDTO> getWikiPageById(@PathVariable Integer id) {
        WikiPageDTO page = new WikiPageDTO(wikiPageService.findById(id));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<WikiPageDTO> getWikiPageByKey(@PathVariable String key) {
        WikiPageDTO page = new WikiPageDTO(wikiPageService.findByKey(key));
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<WikiPageDTO> createWikiPage(@Validated @RequestBody WikiPageRequestDTO requestDTO) {
        WikiPageDTO createdPage = new WikiPageDTO(wikiPageService.create(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WikiPageDTO> updateWikiPage(@PathVariable Integer id, @Validated @RequestBody WikiPageRequestDTO requestDTO) {
        WikiPageDTO updatedPage = new WikiPageDTO(wikiPageService.update(id, requestDTO));
        return ResponseEntity.ok(updatedPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWikiPage(@PathVariable Integer id) {
        wikiPageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
