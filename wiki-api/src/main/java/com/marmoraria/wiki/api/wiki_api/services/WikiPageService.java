package com.marmoraria.wiki.api.wiki_api.services;


import com.marmoraria.wiki.api.wiki_api.domain.WikiPage;
import com.marmoraria.wiki.api.wiki_api.dto.WikiPageRequestDTO;
import com.marmoraria.wiki.api.wiki_api.repository.WikiPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WikiPageService {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    public List<WikiPage> findAll( ) {
        return wikiPageRepository.findAll();
    }

    public WikiPage findById(Integer id) {
        return wikiPageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Página da wiki não encontrada com ID: " + id));
    }

    public WikiPage findByKey(String key) {
        return wikiPageRepository.findByKey(key)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Página da wiki não encontrada com a chave: " + key));
    }

    public WikiPage create(WikiPageRequestDTO requestDTO) {
        if (wikiPageRepository.existsByKey(requestDTO.getKey())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma página com a chave: " + requestDTO.getKey());
        }
        WikiPage wikiPage = new WikiPage();
        wikiPage.setKey(requestDTO.getKey());
        wikiPage.setTitle(requestDTO.getTitle());
        wikiPage.setContent(requestDTO.getContent());
        return wikiPageRepository.save(wikiPage);
    }

    public WikiPage update(Integer id, WikiPageRequestDTO requestDTO) {
        WikiPage existingPage = findById(id);

        // Verifica se a chave foi alterada e se a nova chave já existe para outra página
        if (!existingPage.getKey().equals(requestDTO.getKey()) && wikiPageRepository.existsByKey(requestDTO.getKey())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outra página com a chave: " + requestDTO.getKey());
        }

        existingPage.setKey(requestDTO.getKey());
        existingPage.setTitle(requestDTO.getTitle());
        existingPage.setContent(requestDTO.getContent());
        return wikiPageRepository.save(existingPage);
    }

    public void delete(Integer id) {
        WikiPage wikiPage = findById(id);
        wikiPageRepository.delete(wikiPage);
    }

    public boolean existsByKey(String key) {
       return this.wikiPageRepository.existsByKey(key);
    }
}
