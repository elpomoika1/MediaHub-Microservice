package grpc

import (
	"context"

	"github.com/elpomoika/mediahub/recommendation/internal/domain"
	pb "github.com/elpomoika/mediahub/recommendation/protos/gen/go/recommendation"
	"github.com/elpomoika/mediahub/recommendation/service"
)

type ServerAPI struct {
	pb.UnimplementedRecommendationServiceServer

	service *service.RecommendationService
}

func NewServer(service *service.RecommendationService) *ServerAPI {
	return &ServerAPI{
		service: service,
	}
}

func (s *ServerAPI) GetSimilarMovies(ctx context.Context, req *pb.GetSimilarMoviesRequest) (*pb.GetSimilarMoviesResponse, error) {
	simFilter := domain.SimilarFilter{
		MediaID:  req.MovieId,
		GenreIDs: req.GenreIds,
	}

	medias, err := s.service.FindSimilarMedias(ctx, &simFilter)
	if err != nil {
		return nil, err
	}

	response := &pb.GetSimilarMoviesResponse{}

	for _, media := range medias {
		response.Movies = append(response.Movies,
			&pb.SimilarMovie{
				MovieId: media.ID,
			},
		)
	}

	return response, nil
}

func (s *ServerAPI) AddMovie(ctx context.Context, req *pb.AddMovieRequest) (*pb.AddMovieResponse, error) {
	err := s.service.CreateMedia(ctx, &domain.Media{
		ID:       req.MovieId,
		GenreIds: req.GenreIds,
	})
	if err != nil {
		return &pb.AddMovieResponse{
			Success: false,
		}, err
	}

	return &pb.AddMovieResponse{
		Success: true,
	}, nil
}
