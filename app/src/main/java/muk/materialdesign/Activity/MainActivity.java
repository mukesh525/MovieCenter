package muk.materialdesign.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import muk.materialdesign.Fragment.Fragment_BlockBuster;
import muk.materialdesign.Fragment.Fragment_Search;
import muk.materialdesign.Fragment.Fragment_Upcoming;
import muk.materialdesign.Fragment.Myfragment;
import muk.materialdesign.Fragment.NavDrawrFragment;
import muk.materialdesign.R;
import muk.materialdesign.Utils.SortListener;
import muk.materialdesign.anim.AnimationUtils;
import muk.materialdesign.basicsyncadapter.SyncUtils;
import muk.materialdesign.tabs.SlidingTabLayout;


public class MainActivity extends BaseActivity implements MaterialTabListener, View.OnClickListener, NavDrawrFragment.ClickListner {
    public static final int MOVIE_SEARCH_RESULT = 0;
    public static final int MOVIE_UPCOMING = 2;
    public static final int MOVIE_BLOCKBUSTER = 1;
    private static final String TAG_SORT_NAME = "sortName";
    private static final String TAG_SORT_DATE = "sortDate";
    private static final String TAG_SORT_RATINGS = "sortRatings";
    private FloatingActionButton mFAB;
    FloatingActionMenu actionMenu;
    private Fragment fragmentsearch;
    String Query="A";
    private QueryChangeListner queryChangeListner;
    private ViewGroup containerAppBar;
    private SearchView searchView;

    private MaterialTabHost mtabs;
    private ViewPager mpager;
    private MyPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        // setContentView(R.layout.activity_main_appbar);
        // setContentView(R.layout.activity_main);
        //   showToolbar(); loadDrawer();
        SyncUtils.CreateSyncAccount(this);
        containerAppBar = (ViewGroup) findViewById(R.id.container_app_bar);
        // AnimationUtils.animateToolbar(containerAppBar);
       /* Fragment fragment=new Fragment1();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.MainContent,fragment).commit();*/

       /* mtabs= (SlidingTabLayout) findViewById(R.id.tabs);
        mpager= (ViewPager) findViewById(R.id.pager);
        mpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mtabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mtabs.setDistributeEvenly(true);
        mtabs.setBackgroundColor(getResources().getColor(R.color.primary));
        mtabs.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        mtabs.setViewPager(mpager);*/


        mtabs = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mpager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mpager.setAdapter(adapter);


        mpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mtabs.setSelectedNavigationItem(position);
                invalidateOptionsMenu();
            }
        });
        for (int i = 0; i < adapter.getCount(); i++) {
            mtabs.addTab(
                    mtabs.newTab()
                            // .setText(adapter.getPageTitle(i))
                            .setIcon(adapter.getIcon(i))
                            .setTabListener(this));
        }
        buildFAB();
    }


    private void buildFAB() {
        //define the icon for the main floating action button
        ImageView iconActionButton = new ImageView(this);
        iconActionButton.setImageResource(R.drawable.ic_action_new);

        //set the appropriate background for the main floating action button along with its icon
        mFAB = new FloatingActionButton.Builder(this)
                .setContentView(iconActionButton)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        //define the icons for the sub action buttons
        ImageView iconSortName = new ImageView(this);
        iconSortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView iconSortDate = new ImageView(this);
        iconSortDate.setImageResource(R.drawable.ic_action_calendar);
        ImageView iconSortRatings = new ImageView(this);
        iconSortRatings.setImageResource(R.drawable.ic_action_important);

        //set the background for all the sub buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button_gray));


        //build the sub buttons
        SubActionButton buttonSortName = itemBuilder.setContentView(iconSortName).build();
        SubActionButton buttonSortDate = itemBuilder.setContentView(iconSortDate).build();
        SubActionButton buttonSortRatings = itemBuilder.setContentView(iconSortRatings).build();

        buttonSortName.setTag(TAG_SORT_NAME);
        buttonSortDate.setTag(TAG_SORT_DATE);
        buttonSortRatings.setTag(TAG_SORT_RATINGS);

        buttonSortName.setOnClickListener(this);
        buttonSortDate.setOnClickListener(this);
        buttonSortRatings.setOnClickListener(this);


        //add the sub buttons to the main floating action button
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortName)
                .addSubActionView(buttonSortDate)
                .addSubActionView(buttonSortRatings)
                .attachTo(mFAB)
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search Movies");
        searchItem.setVisible(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.setIconified(true);
                if (query.length() != 0) {
                 Fragment fragment = (Fragment) adapter.instantiateItem(mpager, mpager.getCurrentItem());
                 if (fragment instanceof QueryChangeListner) {
                     //if(queryChangeListner!=null)
                     ((QueryChangeListner) fragment).onQueryChange(query);
                    // queryChangeListner.onQueryChange(query);
                 }
                 // Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                    searchView.setIconified(true);
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.hasFocus()) {
            searchView.clearFocus();
        }
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int page = mpager.getCurrentItem();
        if (page == 0) {
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            menu.findItem(R.id.action_search).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       /* //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Setting Button Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.navigate) {

            // startActivity(new Intent(this, SubActivity.class));
            // overridePendingTransition(0,0);
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        if (materialTab.getPosition() == 0) {
            if (searchView.getVisibility() == View.GONE)
                searchView.setVisibility(View.VISIBLE);
        } else {
            if (searchView.getVisibility() == View.VISIBLE)
                searchView.setVisibility(View.GONE);
        }
        mpager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = (Fragment) adapter.instantiateItem(mpager, mpager.getCurrentItem());
        if (fragment instanceof SortListener) {

            if (v.getTag().equals(TAG_SORT_NAME)) {
                ((SortListener) fragment).onSortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                ((SortListener) fragment).onSortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATINGS)) {
                ((SortListener) fragment).onSortByRating();
            }
        }
    }

    @Override
    public void OnClick(View view, int position) {
        mpager.setCurrentItem(position);

    }

    @Override
    public void onDrawerSlide(float slideOffset) {
        toggleTranslateFAB(slideOffset);
    }


    private void toggleTranslateFAB(float slideOffset) {
        if (actionMenu != null) {
            if (actionMenu.isOpen()) {
                actionMenu.close(true);
            }
            mFAB.setTranslationX(slideOffset * 200);
        }
    }


    class MyPagerAdapter extends FragmentPagerAdapter {
        // int[] icons = {R.drawable.ic_action_home, R.drawable.ic_action_articles, R.drawable.ic_action_personal};
        int[] icons = {R.drawable.ic_action_search_icon_512, R.drawable.ic_action_trending, R.drawable.ic_action_movie_noplay_blank};
        String[] tabs;
        String query;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);

      }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case MOVIE_SEARCH_RESULT:

                     fragment = Fragment_Search.newInstance(Query,"");
                   queryChangeListner= (QueryChangeListner) fragment;
                    break;
                case MOVIE_BLOCKBUSTER:

                    fragment = Fragment_BlockBuster.newInstance("", "");
                    break;
                case MOVIE_UPCOMING:

                    fragment = Fragment_Upcoming.newInstance("", "");
                    break;
            }
            return fragment;
        }

       /* @Override
        public int getItemPosition(Object object) {
            return adapter.POSITION_NONE;
        }*/

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable = getResources().getDrawable(icons[position]);
            drawable.setBounds(0, 0, 36, 36);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 3;
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }


/*
 @Override
        public int getItemPosition(Object object) {

            return POSITION_NONE;
        }*/
    }



    public interface QueryChangeListner
    {
        public void onQueryChange(String Query);
    }
}
