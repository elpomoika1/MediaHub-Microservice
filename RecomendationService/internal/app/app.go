package app

import (
	"net/http"

	"github.com/go-chi/chi/v5"
)

type App struct {
	router *chi.Mux
	server *http.Server
}

func New() (*App, error) {
	router := newRouter()

	server := &http.Server{
		Addr:    ":8086",
		Handler: router,
	}

	return &App{
		router: router,
		server: server,
	}, nil
}

func (app *App) Run() {
	app.server.ListenAndServe()
}
