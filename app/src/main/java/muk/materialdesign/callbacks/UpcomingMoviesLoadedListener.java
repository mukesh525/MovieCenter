package muk.materialdesign.callbacks;

import java.util.ArrayList;

import muk.materialdesign.Model.Movie;


/**
 * Created by Windows on 13-04-2015.
 */
public interface UpcomingMoviesLoadedListener {
    public void onUpcomingMoviesLoaded(ArrayList<Movie> listMovies);
}
