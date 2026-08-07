package service

import (
	"context"

	"github.com/elpomoika/mediahub/recommendation/internal/domain"
)

type RecommendationService struct {
	repo MediaRepository
}

func NewRecommendationService(repo MediaRepository) *RecommendationService {
	return &RecommendationService{
		repo: repo,
	}
}

func (s *RecommendationService) FindSimilarMedias(ctx context.Context, req *domain.SimilarFilter) ([]domain.Media, error) {
	return s.repo.FindSimilarMedia(ctx, req)
}

func (s *RecommendationService) CreateMedia(ctx context.Context, media *domain.Media) error {
	return s.repo.CreateMedia(ctx, media)
}
