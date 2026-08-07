package postgres

import (
	"context"

	"github.com/elpomoika/mediahub/recommendation/internal/domain"
	"github.com/elpomoika/mediahub/recommendation/service"

	"github.com/jackc/pgx/v5/pgxpool"
)

type PostgresMediaRepository struct {
	pool *pgxpool.Pool
}

func NewPostgresMediaRepository(pool *pgxpool.Pool) service.MediaRepository {
	return &PostgresMediaRepository{pool: pool}
}

func (r *PostgresMediaRepository) CreateMedia(ctx context.Context, req *domain.Media) error {
	tx, err := r.pool.Begin(ctx)
	if err != nil {
		return err
	}

	defer tx.Rollback(ctx)

	query := `
		INSERT INTO media (movie_id)
		VALUES ($1)
	`

	_, err = r.pool.Exec(ctx, query, req.ID)
	if err != nil {
		return err
	}

	query = `
		INSERT INTO media_genres
		VALUES ($1, $2)
	`

	for _, genre := range req.GenreIds {
		_, err = tx.Exec(ctx, query, req.ID, genre)
		if err != nil {
			return err
		}
	}

	if err := tx.Commit(ctx); err != nil {
		return err
	}

	return nil
}

func (r *PostgresMediaRepository) DeleteMedia(ctx context.Context, mediaId int64) error {
	query := `
		DELETE FROM media
		WHERE movie_id = $1
	`

	_, err := r.pool.Exec(ctx, query, mediaId)
	return err
}

func (r *PostgresMediaRepository) FindSimilarMedia(ctx context.Context, request *domain.SimilarFilter) ([]domain.Media, error) {
	query := `
		SELECT mg.media_id
		FROM media_genres mg
		WHERE mg.genre_id = ANY($1)
			AND mg.media_id <> $2
		GROUP BY mg.media_id
		ORDER BY COUNT(*) DESC
		LIMIT 10
	`

	rows, err := r.pool.Query(ctx, query, request.GenreIDs, request.MediaID)
	if err != nil {
		return nil, err
	}

	var medias []domain.Media
	for rows.Next() {
		var media domain.Media
		err := rows.Scan(
			&media.ID,
		)
		if err != nil {
			return nil, err
		}
		medias = append(medias, media)
	}

	if err := rows.Err(); err != nil {
		return nil, err
	}

	return medias, nil
}
