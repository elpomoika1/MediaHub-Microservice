package main

import (
	"elpomoika/mediahub/v2/internal/app"
	"log"
)

func main() {
	app, err := app.New()
	if err != nil {
		log.Fatal(err)
		return
	}

	app.Run()
}
