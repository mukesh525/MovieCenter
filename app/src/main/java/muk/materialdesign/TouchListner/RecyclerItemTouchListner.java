package muk.materialdesign.TouchListner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import muk.materialdesign.callbacks.MovieItemClickLisner;

/**
 * Created by Mukesh on 4/23/2015.
 */
public class RecyclerItemTouchListner implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    private MovieItemClickLisner movieItemclickListner;

    public RecyclerItemTouchListner(Context context, final RecyclerView recyclerView, final MovieItemClickLisner movieItemclickListner) {
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
}
