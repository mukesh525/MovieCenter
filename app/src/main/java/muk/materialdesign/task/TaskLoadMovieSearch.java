package muk.materialdesign.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import muk.materialdesign.Model.Movie;
import muk.materialdesign.Utils.MovieUtils;
import muk.materialdesign.callbacks.BoxOfficeMoviesLoadedListener;
import muk.materialdesign.callbacks.SearchMoviesLoadedListener;
import muk.materialdesign.json.Parser;
import muk.materialdesign.json.UrlEndpoints;
import muk.materialdesign.network.VolleySingleTon;

/**
 * Created by Mukesh on 4/20/2015.
 */
public class TaskLoadMovieSearch extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private SearchMoviesLoadedListener myComponent;
    private VolleySingleTon volleySingleton;
    private RequestQueue requestQueue;
    private String  query;
    static ArrayList<Movie> listMovies = new ArrayList<>();

    public TaskLoadMovieSearch(SearchMoviesLoadedListener myComponent,String query) {

        this.myComponent = myComponent;
        this.query=query;
        volleySingleton = VolleySingleTon.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

    }

    public  void  sendJasonRequest()
    {
        JsonObjectRequest request = new   JsonObjectRequest(Request.Method.GET,
                UrlEndpoints.getRequestUrlMoviesSearch(30,query)
                , (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Respone:----------", response.toString());
                        if (response != null || response.length() != 0)
                        {
                            listMovies= Parser.parseMoviesJSON(response);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myComponent.onSearchMoviesErrorListener(error);
                    }
                });
        requestQueue.add(request);
    }



    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        listMovies= MovieUtils.loadMovieSearch(requestQueue, query);
        return listMovies;
    }
    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onSearchMoviesLoadedListener(listMovies);
        }
    }
}
