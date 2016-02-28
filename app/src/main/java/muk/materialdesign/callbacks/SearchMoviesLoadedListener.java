package muk.materialdesign.callbacks;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import muk.materialdesign.Model.Movie;

public interface SearchMoviesLoadedListener {
    public void onSearchMoviesLoadedListener(ArrayList<Movie> listMovies);
    public void onSearchMoviesErrorListener(VolleyError error);
}
