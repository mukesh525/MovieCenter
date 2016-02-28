package muk.materialdesign.Fragment;


import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import muk.materialdesign.Adapter.AdapterBoxOffice;
import muk.materialdesign.DataBase.DBMovies;
import muk.materialdesign.Activity.Detail;
import muk.materialdesign.Model.Movie;
import muk.materialdesign.R;
import muk.materialdesign.TouchListner.RecyclerItemTouchListner;
import muk.materialdesign.Utils.Conect;
import muk.materialdesign.Utils.Keys;
import muk.materialdesign.Utils.L;
import muk.materialdesign.Utils.MovieSorter;
import muk.materialdesign.Utils.MyApplication;
import muk.materialdesign.Utils.SortListener;
import muk.materialdesign.basicsyncadapter.SyncUtils;
import muk.materialdesign.callbacks.BoxOfficeMoviesLoadedListener;
import muk.materialdesign.callbacks.MovieItemClickLisner;
import muk.materialdesign.network.VolleySingleTon;
import muk.materialdesign.task.TaskLoadMoviesBoxOffice;


public class Fragment_BlockBuster extends Fragment implements SortListener, SwipeRefreshLayout.OnRefreshListener, BoxOfficeMoviesLoadedListener, ObservableScrollViewCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIES = "state_movies";


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


    // TODO: Rename and change types and number of parameters
    public static Fragment_BlockBuster newInstance(String param1, String param2) {
        Fragment_BlockBuster fragment = new Fragment_BlockBuster();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_BlockBuster() {
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
/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Create account, if needed
        SyncUtils.CreateSyncAccount(activity);
    }*/



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__block_buster, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        ObservableRecyclerView ListMovieHits = (ObservableRecyclerView) view.findViewById(R.id.listMovieHits);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeMovieHits);
        swipeRefreshLayout.setOnRefreshListener(this);
        ListMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        // adapterBoxOffice.setMovieClickedListner(this);
        dbMovies = MyApplication.getWritableDatabase();
        ListMovieHits.setAdapter(adapterBoxOffice);
        ListMovieHits.setScrollViewCallbacks(this);
        ListMovieHits.setClickable(true);
        ListMovieHits.setFocusable(true);
        ListMovieHits.setFocusableInTouchMode(true);
        // ListMovieHits.seton
        if (savedInstanceState != null) {
            listMovie = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            //adapterBoxOffice.setMovies(listMovie);

        } else {
            listMovie = MyApplication.getWritableDatabase().readMovies(DBMovies.BOX_OFFICE);
            //if the database is empty, trigger an AsycnTask to download movie list from the web
            if (listMovie.isEmpty()) {
                if (Conect.isInternateOn(getActivity())) {
                    new TaskLoadMoviesBoxOffice(this).execute();
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
                L.m("FragmentUpcoming: executing task from fragment");

            }

        }
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
        //sendJsonRequest();
        if (Conect.isInternateOn(getActivity())) {
            new TaskLoadMoviesBoxOffice(this).execute();
        } else {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (listMovies != null && listMovies.size() > 0) {
            adapterBoxOffice.setMovies(listMovies);
        } else {
            if (Conect.isInternateOn(getActivity()))
                Toast.makeText(getActivity(), "No Response From Server Try again", Toast.LENGTH_LONG).show();

        }

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

    /*@Override
    public void OnMovieItemClick(View view, int position) {
        //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
    }*/

   /* class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private MovieItemClickLisner movieItemclickListner;

        public RecyclerTouchListner(Context context, final RecyclerView recyclerView, final MovieItemClickLisner movieItemclickListner) {
            this.movieItemclickListner = movieItemclickListner;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && movieItemclickListner != null) {
                        // clickListner.OnLongClick(child, recyclerView.getChildPosition(child));

                    }

                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && movieItemclickListner != null && gestureDetector.onTouchEvent(e)) {
                movieItemclickListner.OnMovieItemClick(child, rv.getChildPosition(child));

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }*/

}
