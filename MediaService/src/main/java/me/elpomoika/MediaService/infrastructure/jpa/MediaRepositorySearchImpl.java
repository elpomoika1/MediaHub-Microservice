package me.elpomoika.MediaService.infrastructure.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.elpomoika.MediaService.domain.entity.Media;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;

@RequiredArgsConstructor
public class MediaRepositorySearchImpl implements MediaRepositorySearch {
    private final EntityManager entityManager;

    public List<Media> searchMedia(String title) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<Media> result = searchSession.search(Media.class)
                .where(f -> f.match()
                        .fields("title")
                        .matching(title)
                        .fuzzy())
                .fetch(20);
        return result.hits();
    }
}