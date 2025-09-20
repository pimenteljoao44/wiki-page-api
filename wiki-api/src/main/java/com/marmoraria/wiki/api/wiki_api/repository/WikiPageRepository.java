package com.marmoraria.wiki.api.wiki_api.repository;

import com.marmoraria.wiki.api.wiki_api.domain.WikiPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WikiPageRepository extends JpaRepository<WikiPage, Integer> {
    Optional<WikiPage> findByKey(String key);
    boolean existsByKey(String key);
}
