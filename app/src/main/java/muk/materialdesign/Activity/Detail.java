package muk.materialdesign.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import muk.materialdesign.Model.Movie;
import muk.materialdesign.R;
import muk.materialdesign.Utils.Conect;
import muk.materialdesign.Utils.Constants;
import muk.materialdesign.Utils.Keys;
import muk.materialdesign.callbacks.MovieItemClickLisner;
import muk.materialdesign.network.VolleySingleTon;


public class Detail extends ActionBarActivity {
    ImageView movieThumbnail;
    TextView movieTitle;
    TextView movieReleaseDate;
    TextView Synopsis; private Toolbar toolbar;
    RatingBar movieAudienceScore;
    private ImageLoader imageLoader;
    Movie currentMovie; private VolleySingleTon volleySingleton;

    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_detail);
        movieThumbnail = (ImageView) findViewById(R.id.movieThumbnail);
        movieTitle = (TextView)findViewById(R.id.movieTitle);
        movieReleaseDate = (TextView) findViewById(R.id.movieReleaseDate);
        Synopsis = (TextView) findViewById(R.id.synopsis);
        movieAudienceScore = (RatingBar)findViewById(R.id.movieAudienceScore);
        volleySingleton = VolleySingleTon.getInstance();
        imageLoader = volleySingleton.getImageLoader();
        showToolbar();
        currentMovie= getIntent().getExtras().getParcelable("Movie");
        movieTitle.setText(currentMovie.getTitle());
        Date movieReleaseDatee = currentMovie.getReleaseDateTheater();
        if (movieReleaseDatee != null) {
            String formattedDate = dateFormatter.format(movieReleaseDatee);
            movieReleaseDate.setText(formattedDate);
        } else {
            movieReleaseDate.setText(Constants.NA);
        }

        int audienceScore = currentMovie.getAudienceScore();
        if (audienceScore == -1) {
            movieAudienceScore.setRating(0.0F);
            movieAudienceScore.setAlpha(0.5F);
        } else {
            movieAudienceScore.setRating(audienceScore / 20.0F);
            movieAudienceScore.setAlpha(1.0F);
        }
       Synopsis.setText(currentMovie.getSynopsis());
        String urlThumnail = currentMovie.getUrlThumbnail();
       loadImages(urlThumnail);


    }
    public void showToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void loadImages(String urlThumbnail) {
        if (!urlThumbnail.equals(Constants.NA)) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    movieThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
