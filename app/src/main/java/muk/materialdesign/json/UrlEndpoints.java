package muk.materialdesign.json;

import muk.materialdesign.Utils.MyApplication;

/**
 * Created by Windows on 10-02-2015.
 */
public class UrlEndpoints {
    public static final String URL_BOX_OFFICE = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
    public static final String URL_UPCOMING = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json";
    public static final String URL_SEARCH = "http://api.rottentomatoes.com/api/public/v1.0/movies.json";
    public static final String URL_CHAR_QUESTION = "?";
    public static final String URL_CHAR_AMEPERSAND = "&";
    public static final String URL_PARAM_API_KEY = "apikey=";
    public static final String URL_SEARCH_KEY = "q=";
    public static final String URL_PARAM_LIMIT = "limit=";

    public static String getRequestUrlBoxOfficeMovies(int limit) {

        return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;
    }

    public static String getRequestUrlMoviesSearch(int limit,String query) {

        return URL_SEARCH
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                +URL_SEARCH_KEY +query
                +URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;
    }

    public static String getRequestUrlUpcomingMovies(int limit) {

        return URL_UPCOMING
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;
    }
}
