package app

import (
	"log"
	"net"

	"github.com/elpomoika/mediahub/recommendation/internal/database"
	grpcserver "github.com/elpomoika/mediahub/recommendation/internal/grpc"
	"github.com/elpomoika/mediahub/recommendation/internal/repository/postgres"
	pb "github.com/elpomoika/mediahub/recommendation/protos/gen/go/recommendation"
	"github.com/elpomoika/mediahub/recommendation/service"
	"github.com/jackc/pgx/v5/pgxpool"
	grpc "google.golang.org/grpc"
)

type App struct {
	server *grpc.Server
	db     *pgxpool.Pool
}

func New() (*App, error) {
	pool, err := database.NewDb()
	if err != nil {
		log.Fatalf("Unable to connect to database %v", err)
		return nil, err
	}

	repo := postgres.NewPostgresMediaRepository(pool)
	service := service.NewRecommendationService(repo)
	handler := grpcserver.NewServer(service)

	grpcServer := grpc.NewServer()

	pb.RegisterRecommendationServiceServer(grpcServer, handler)

	return &App{
		server: grpcServer,
		db:     pool,
	}, nil
}

func (a *App) Close() {
	a.db.Close()
}

func (a *App) Run() error {
	defer a.Close()

	grpcListener, err := net.Listen(
		"tcp",
		":50051",
	)

	if err != nil {
		return err
	}

	return a.server.Serve(grpcListener)

}
