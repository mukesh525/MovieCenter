package muk.materialdesign.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import muk.materialdesign.Fragment.Myfragment;
import muk.materialdesign.Fragment.NavDrawrFragment;
import muk.materialdesign.R;


public class SubActivity extends BaseActivity implements MaterialTabListener ,NavDrawrFragment.ClickListner{
    private ViewPager mpager;
    private MaterialTabHost mtabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_sub);
      //  setContentView(R.layout.activity_sub);
       // showToolbar();// loadDrawer();
        mtabs= (MaterialTabHost) findViewById(R.id.materialTabHost);
        mpager= (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter=new MyPagerAdapter(getSupportFragmentManager());
        mpager.setAdapter(adapter);
        mpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mtabs.setSelectedNavigationItem(position);
            }
        });
        for(int i=0;i<adapter.getCount();i++)
        {
            mtabs.addTab(
                    mtabs.newTab()
                            // .setText(adapter.getPageTitle(i))
                            .setIcon(adapter.getIcon(i))
                            .setTabListener(this));
         }
       }


    private void buildFAB() {
        //define the icon for the main floating action button
        ImageView iconActionButton = new ImageView(this);
        iconActionButton.setImageResource(R.drawable.ic_action_new);

        //set the appropriate background for the main floating action button along with its icon
      /*  FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(iconActionButton)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();*/

        //define the icons for the sub action buttons
        ImageView iconSortName = new ImageView(this);
        iconSortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView iconSortDate = new ImageView(this);
        iconSortDate.setImageResource(R.drawable.ic_action_calendar);
        ImageView iconSortRatings = new ImageView(this);
        iconSortRatings.setImageResource(R.drawable.ic_action_important);

        //set the background for all the sub buttons
       /* SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
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
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortName)
                .addSubActionView(buttonSortDate)
                .addSubActionView(buttonSortRatings)
                .attachTo(actionButton)
                .build();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mpager.setCurrentItem(materialTab.getPosition());


    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void OnClick(View view, int position) {

    }

    @Override
    public void onDrawerSlide(float slideOffset) {

    }


    class MyPagerAdapter  extends FragmentPagerAdapter
    {
        int[] icons={R.drawable.ic_action_home,R.drawable.ic_action_articles,R.drawable.ic_action_personal};
        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs=getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {

            return Myfragment.getInstance(position);
        }

        public Drawable getIcon(int position) {

            return getResources().getDrawable(icons[position]);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    /*public static class Myfragment extends Fragment
    {
        private TextView textview;
        public static Myfragment getInstance(int position)
        {
            Myfragment myfragment=new Myfragment();
            Bundle args= new Bundle();
            args.putInt("position",position);
            myfragment.setArguments(args);
            return myfragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout=inflater.inflate(R.layout.my_fragment,container,false);
            textview= (TextView) layout.findViewById(R.id.position);
            Bundle args=getArguments();
            if(args!=null)
            {
                textview.setText("The Page Selected is "+args.getInt("position"));
            }
            return layout;
        }
    }*/
}
