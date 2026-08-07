CREATE TABLE media (
    media_id BIGINT PRIMARY KEY
)

CREATE TABLE media_genres (
    media_id BIGINT NOT NULL REFERENCES media(media_id) ON DELETE CASCADE,
    genre_id BIGINT NOT NULL REFERENCES genres(id) ON DELETE CASCADE,

    PRIMARY KEY(media_id, genre_id)
)
