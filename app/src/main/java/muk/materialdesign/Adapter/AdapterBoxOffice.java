package muk.materialdesign.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import muk.materialdesign.Model.Movie;
import muk.materialdesign.R;
import muk.materialdesign.Utils.Constants;
import muk.materialdesign.anim.AnimationUtils;
import muk.materialdesign.callbacks.MovieItemClickLisner;
import muk.materialdesign.network.VolleySingleTon;


public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.ViewHolderBoxOffice> {

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleTon volleySingleton;
    private ImageLoader imageLoader;
    private MovieItemClickLisner movieItemClickLisner;
    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private int previousPosition=0;


    public AdapterBoxOffice(Context context) {

        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleTon.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        notifyDataSetChanged();
    }
    public void setMovieClickedListner(MovieItemClickLisner movieItemClickLisner)
    {
        this.movieItemClickLisner=movieItemClickLisner;
    }
    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
       // View view = layoutInflater.inflate(R.layout.custom_movie_box_office, parent, false);
        View view = layoutInflater.inflate(R.layout.card_layout, parent, false);
        ViewHolderBoxOffice viewHolder = new ViewHolderBoxOffice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {
        Movie currentMovie = listMovies.get(position);
        holder.movieTitle.setText(currentMovie.getTitle());
        Date movieReleaseDate = currentMovie.getReleaseDateTheater();
        if (movieReleaseDate != null) {
            String formattedDate = dateFormatter.format(movieReleaseDate);
            holder.movieReleaseDate.setText(formattedDate);
        } else {
            holder.movieReleaseDate.setText(Constants.NA);
        }

        int audienceScore = currentMovie.getAudienceScore();
        if (audienceScore == -1) {
            holder.movieAudienceScore.setRating(0.0F);
            holder.movieAudienceScore.setAlpha(0.5F);
        } else {
            holder.movieAudienceScore.setRating(audienceScore / 20.0F);
            holder.movieAudienceScore.setAlpha(1.0F);
        }

        if(position>previousPosition)
        {
            AnimationUtils.animate(holder, true);
        }
        else{
            AnimationUtils.animate(holder, false);
        }
        previousPosition=position;


        String urlThumnail = currentMovie.getUrlThumbnail();
        loadImages(urlThumnail, holder);

    }


    private void loadImages(String urlThumbnail, final ViewHolderBoxOffice holder) {
        if (!urlThumbnail.equals(Constants.NA)) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.movieThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    class ViewHolderBoxOffice extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movieThumbnail;
        TextView movieTitle;
        TextView movieReleaseDate;
        RatingBar movieAudienceScore;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            movieAudienceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
            //itemView.setClickable(true);
           // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(movieItemClickLisner!=null)
            {
               // movieItemClickLisner.OnMovieItemClick(v,getPosition());
                movieItemClickLisner.OnMovieItemClick(v,getAdapterPosition());
            }

        }
    }
}
