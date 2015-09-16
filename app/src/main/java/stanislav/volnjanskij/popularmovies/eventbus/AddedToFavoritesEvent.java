package stanislav.volnjanskij.popularmovies.eventbus;

import stanislav.volnjanskij.popularmovies.api.MovieModel;

/**
 * Created by Stas on 16.09.15.
 */
public class AddedToFavoritesEvent {
    MovieModel movie;

    public AddedToFavoritesEvent(MovieModel movie) {
        this.movie = movie;
    }

    public MovieModel getMovie() {
        return movie;
    }
}
