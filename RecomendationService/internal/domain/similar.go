package domain

type SimilarFilter struct {
	MediaID  int64
	GenreIDs []int64
	Limit    int
}
