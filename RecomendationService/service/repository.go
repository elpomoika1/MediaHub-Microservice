package service

import (
	"context"

	"github.com/elpomoika/mediahub/recommendation/internal/domain"
)

type MediaRepository interface {
	CreateMedia(ctx context.Context, req *domain.Media) error
	DeleteMedia(ctx context.Context, mediaId int64) error
	FindSimilarMedia(ctx context.Context, req *domain.SimilarFilter) ([]domain.Media, error)
}
