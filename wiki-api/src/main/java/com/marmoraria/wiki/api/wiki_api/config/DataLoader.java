package com.marmoraria.wiki.api.wiki_api.config;


import com.marmoraria.wiki.api.wiki_api.dto.WikiPageRequestDTO;
import com.marmoraria.wiki.api.wiki_api.services.WikiPageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataLoader {

    private final ResourceLoader resourceLoader;
    private final WikiPageService wikiPageService;

    public DataLoader(ResourceLoader resourceLoader, WikiPageService wikiPageService) {
        this.resourceLoader = resourceLoader;
        this.wikiPageService = wikiPageService;
    }

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            List<String> markdownFiles = Arrays.asList(
                    "introducao.md",
                    "visao-geral.md",
                    "arquitetura.md",
                    "banco-dados.md"
            );

            for (String filename : markdownFiles) {
                String key = filename.replace(".md", "");
                if (!wikiPageService.existsByKey(key)) {
                    try {
                        Resource resource = resourceLoader.getResource("classpath:wiki/" + filename);
                        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

                        String title = content.split("\n")[0].replace("# ", "").trim();

                        WikiPageRequestDTO requestDTO = new WikiPageRequestDTO();
                        requestDTO.setKey(key);
                        requestDTO.setTitle(title);
                        requestDTO.setContent(content);

                        wikiPageService.create(requestDTO);
                        System.out.println("Página " + title + " carregada com sucesso.");
                    } catch (IOException e) {
                        System.err.println("Erro ao carregar arquivo Markdown " + filename + ": " + e.getMessage());
                    }
                } else {
                    System.out.println("Página com chave " + key + " já existe. Pulando carregamento.");
                }
            }
        };
    }
}
