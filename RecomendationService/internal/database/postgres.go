package database

import (
	"context"

	"github.com/jackc/pgx/v5/pgxpool"
)

func NewDb() (*pgxpool.Pool, error) {
	ctx := context.Background()
	pool, err := pgxpool.New(ctx, "")
	if err != nil {
		return nil, err
	}

	if err := pool.Ping(ctx); err != nil {
		return nil, err
	}

	return pool, nil
}
