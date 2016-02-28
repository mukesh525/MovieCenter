package muk.materialdesign.Utils;

import android.util.Log;

import com.android.volley.RequestQueue;

import org.json.JSONObject;


import java.util.ArrayList;

import muk.materialdesign.DataBase.DBMovies;
import muk.materialdesign.Model.Movie;
import muk.materialdesign.json.Parser;
import muk.materialdesign.json.Requestor;
import muk.materialdesign.json.UrlEndpoints;


/**
 * Created by Windows on 02-03-2015.
 */
public class MovieUtils {
    public static ArrayList<Movie> loadBoxOfficeMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, UrlEndpoints.getRequestUrlBoxOfficeMovies(30));
        ArrayList<Movie> listMovies  = Parser.parseMoviesJSON(response);
        if (listMovies.size() > 0)

        {
            Log.i("SyncAdapter", "Updating database for MoviesHits");

            MyApplication.getWritableDatabase().insertMovies(DBMovies.BOX_OFFICE, listMovies, true);
        }
            return listMovies;


    }

    public static ArrayList<Movie> loadMovieSearch(RequestQueue requestQueue, String query) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, UrlEndpoints.getRequestUrlMoviesSearch(30, query));
        ArrayList<Movie> listMovies = Parser.parseMoviesJSON(response);
        if (listMovies.size() > 0){
             MyApplication.getWritableDatabase().insertMovies(DBMovies.SEARCH, listMovies, true);}


        return listMovies;
    }

    public static ArrayList<Movie> loadUpcomingMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, UrlEndpoints.getRequestUrlUpcomingMovies(30));
        ArrayList<Movie> listMovies = Parser.parseMoviesJSON(response);
        if (listMovies.size() > 0){
            Log.i("SyncAdapter", "Updating database for MoviesUpcoming");
            MyApplication.getWritableDatabase().insertMovies(DBMovies.UPCOMING, listMovies, true);}


     return listMovies;
}
}
