package muk.materialdesign.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;

import muk.materialdesign.Activity.Detail;
import muk.materialdesign.Activity.MainActivity;
import muk.materialdesign.Adapter.AdapterBoxOffice;
import muk.materialdesign.DataBase.DBMovies;
import muk.materialdesign.Model.Movie;
import muk.materialdesign.R;
import muk.materialdesign.TouchListner.RecyclerItemTouchListner;
import muk.materialdesign.Utils.Conect;
import muk.materialdesign.Utils.L;
import muk.materialdesign.Utils.MovieSorter;
import muk.materialdesign.Utils.MyApplication;
import muk.materialdesign.Utils.SortListener;
import muk.materialdesign.basicsyncadapter.SyncUtils;
import muk.materialdesign.callbacks.MovieItemClickLisner;
import muk.materialdesign.callbacks.SearchMoviesLoadedListener;
import muk.materialdesign.network.VolleySingleTon;
import muk.materialdesign.task.TaskLoadMovieSearch;
import muk.materialdesign.task.TaskLoadMoviesBoxOffice;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Search extends Fragment implements SortListener,SwipeRefreshLayout.OnRefreshListener,SearchMoviesLoadedListener,MainActivity.QueryChangeListner, ObservableScrollViewCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIES = "state_movies_search";

 // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textVolleyError;
    private VolleySingleTon volleySingleton;
    private RecyclerView ListMovieHits;
    private DBMovies dbMovies;
    private MovieSorter movieSorter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private AdapterBoxOffice adapterBoxOffice;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Search newInstance(String param1, String param2) {
        Fragment_Search fragment = new Fragment_Search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_Search() {
        movieSorter = new MovieSorter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment__search, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
      // ListMovieHits = (RecyclerView) view.findViewById(R.id.listMovieHits);
        ObservableRecyclerView ListMovieHits = (ObservableRecyclerView) view.findViewById(R.id.listMovieHits);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeMovieHits);
        swipeRefreshLayout.setOnRefreshListener(this);
        ListMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListMovieHits.setScrollViewCallbacks(this);
        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        //adapterBoxOffice.setMovieClickedListner(this);
        dbMovies = MyApplication.getWritableDatabase();
        ListMovieHits.setAdapter(adapterBoxOffice);
        if (savedInstanceState != null) {
            listMovie = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
        }
        else {
            listMovie = MyApplication.getWritableDatabase().readMovies(DBMovies.SEARCH);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (listMovie.isEmpty())
            {
              if (Conect.isInternateOn(getActivity())) {
                    new TaskLoadMovieSearch(this, mParam1).execute();
                }
              else {
                  Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
              }
            }
        }// new TaskLoadMovieSearch(this,mParam1).sendJasonRequest();

        adapterBoxOffice.setMovies(listMovie);
        ListMovieHits.addOnItemTouchListener(new RecyclerItemTouchListner(getActivity(), ListMovieHits, new MovieItemClickLisner() {
            @Override
            public void OnMovieItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), Detail.class);
                Movie movie=listMovie.get(position);
                i.putExtra("Movie",movie);
                startActivity(i);
            }
        }));

        return view;
    }

  /*  public void setQuery(String query) {
      this.query = query;
        adapterBoxOffice.notifyDataSetChanged();
    }*/

    @Override
    public void onSortByName() {
        movieSorter.sortMoviesByName(listMovie);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByDate() {
        movieSorter.sortMoviesByDate(listMovie);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByRating() {
        movieSorter.sortMoviesByRating(listMovie);
        adapterBoxOffice.notifyDataSetChanged();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, listMovie);
    }
    @Override
    public void onRefresh() {
        if (Conect.isInternateOn(getActivity())) {
            new TaskLoadMovieSearch(this, mParam1).execute();
        }
        else{
            if (swipeRefreshLayout.isRefreshing())
            {
              swipeRefreshLayout.setRefreshing(false);
            }
        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSearchMoviesLoadedListener(ArrayList<Movie> listMovies) {
        if (swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }
        if(listMovies!=null &&listMovies.size()>0)
        {
           adapterBoxOffice.setMovies(listMovies);
        }

        else
        {
            if (Conect.isInternateOn(getActivity())&&! mParam1.equals("A")) {
                Toast.makeText(getActivity(), "No result found for " + mParam1, Toast.LENGTH_LONG).show();
                mParam1="A";
            }

        }

    }

    private void handleVolleyError(VolleyError error) {
        textVolleyError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            textVolleyError.setText(R.string.error_timeout);

        } else if (error instanceof AuthFailureError) {
            textVolleyError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof ServerError) {
            textVolleyError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof NetworkError) {
            textVolleyError.setText(R.string.error_network);
            //TODO
        } else if (error instanceof ParseError) {
            textVolleyError.setText(R.string.error_parser);
            //TODO
        }
    }

    @Override
    public void onSearchMoviesErrorListener(VolleyError error) {
            handleVolleyError(error);
    }

    @Override
    public void onQueryChange(String Query) {

        mParam1=Query;
        if (Conect.isInternateOn(getActivity())) {
            new TaskLoadMovieSearch(this, mParam1).execute();
        }
        else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
       // new TaskLoadMovieSearch(this,mParam1).execute();
       // new TaskLoadMovieSearch(this,mParam1).sendJasonRequest();

    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = ((ActionBarActivity)getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }


  /*  @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Create account, if needed
        SyncUtils.CreateSyncAccount(activity);
    }*/

}
