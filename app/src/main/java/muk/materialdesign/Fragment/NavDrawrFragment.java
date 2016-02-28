package muk.materialdesign.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import muk.materialdesign.Activity.MainActivity;
import muk.materialdesign.Adapter.RecylerAdapter;
import muk.materialdesign.Model.Information;
import muk.materialdesign.R;


public class NavDrawrFragment extends Fragment implements RecylerAdapter.ClickedListner {

    private RecyclerView recylerView;
    private ActionBarDrawerToggle mdrawerToggle;
    private DrawerLayout mdrawerlayout;
    private Boolean mUserlearnedDrawer;
    private Boolean mFromsavedInstanceState = false;
    private View ContainerView;
    private RelativeLayout cover;
    private RecylerAdapter adapter;
    private ClickListner ClickListner;
    ImageView imageView;


    public static final String PREF_FILE_NAME = "TestPref";
    public static final String KEY_USER_LEARNED_DRAWER = "User_Learned_Drawer";

    public NavDrawrFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserlearnedDrawer = Boolean.valueOf(ReadFromPrefrences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            //  mUserlearnedDrawer=true;
            mFromsavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_nav_drawr, container, false);
        recylerView = (RecyclerView) layout.findViewById(R.id.drawerlist);

        RecylerAdapter adapter = new RecylerAdapter(getActivity(), getData());
        adapter.setClickedListner(this);
        recylerView.setAdapter(adapter);
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylerView.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), recylerView, new ClickListner() {
            @Override
            public void OnClick(View view, int position) {
                ClickListner.OnClick(view, position - 1);
                //Handle IntentsHere
                mdrawerlayout.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onDrawerSlide(float slideOffset) {
                ClickListner.onDrawerSlide(slideOffset);
           }}));
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ClickListner = (NavDrawrFragment.ClickListner) activity;
    }

    public void setup(int fragmnentId, final DrawerLayout drawerlayout, final Toolbar toolbar) {
        mdrawerlayout = drawerlayout;
        ContainerView = getActivity().findViewById(fragmnentId);


        mdrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerlayout, toolbar, R.string.DrawerOpen, R.string.DrawerClose) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserlearnedDrawer) {
                    mUserlearnedDrawer = true;
                    SaveToPrefrences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserlearnedDrawer + "");
                }

                getActivity().invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
               // ((MainActivity) getActivity()).onDrawerSlide(slideOffset);
                ClickListner.onDrawerSlide(slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };


        if (!mUserlearnedDrawer && !mFromsavedInstanceState) {
            mdrawerlayout.openDrawer(ContainerView);
        }
        mdrawerlayout.setDrawerListener(mdrawerToggle);
        mdrawerlayout.post(new Runnable() {
            @Override
            public void run() {
                mdrawerToggle.syncState();
            }
        });

    }

    class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListner(Context context, final RecyclerView recyclerView, final ClickListner clickListner) {
            this.clickListner = clickListner;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListner != null) {
                       // clickListner.OnLongClick(child, recyclerView.getChildPosition(child));

                    }

                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListner != null && gestureDetector.onTouchEvent(e)) {
                clickListner.OnClick(child, rv.getChildPosition(child));

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }


    public static void SaveToPrefrences(Context context, String prefrencename, String prefrencevalue) {
        SharedPreferences sharedprefrence = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedprefrence.edit();
        editor.putString(prefrencename, prefrencevalue);
        editor.apply();
    }

    public static String ReadFromPrefrences(Context context, String prefrencename, String defaultvalue) {
        SharedPreferences sharedprefrence = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedprefrence.getString(prefrencename, defaultvalue);
    }

    public List<Information> getData() {
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_action_search_icon_512, R.drawable.ic_action_trending, R.drawable.ic_action_movie_noplay_blank};
        String[] title = getResources().getStringArray(R.array.tabs);
        for (int i = 0; i < title.length && i < icons.length; i++) {
            Information current = new Information();
            current.itemid = icons[i];
            current.title = title[i];
            data.add(current);
        }
        return data;

    }

    @Override
    public void OnItemClick(View view, int position) {
        //startActivity(new Intent(getActivity(),SubActivity.class));
        // activityClickedListner.OnItemClick(view,position);
        // mdrawerlayout.closeDrawer(ContainerView);


    }

    public interface ClickListner {
        public void OnClick(View view, int position);

       // public void OnLongClick(View view, int position);

        public void onDrawerSlide(float slideOffset);

    }

}
