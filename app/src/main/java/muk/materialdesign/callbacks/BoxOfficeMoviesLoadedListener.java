package muk.materialdesign.callbacks;

import java.util.ArrayList;

import muk.materialdesign.Model.Movie;


/**
 * Created by Windows on 02-03-2015.
 */
public interface BoxOfficeMoviesLoadedListener {
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies);
}
