package muk.materialdesign.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import muk.materialdesign.Fragment.NavDrawrFragment;
import muk.materialdesign.R;


public abstract class BaseActivity  extends ActionBarActivity {
    private Toolbar toolbar;

    TextView tv;ImageButton btn;
    private ProgressDialog mProgressDialog;

    protected void onCreate(Bundle savedInstanceState,int resLayoutID)
    {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(resLayoutID);
        showToolbar(); loadDrawer();
     }

 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//         MenuItem cart=menu.findItem(R.id.cart);
//         RelativeLayout cartlayout= (RelativeLayout) cart.getActionView();
//        menu.getItem(0).getActionView().setOnClickListener(this);
//         tv= (TextView) cartlayout.findViewById(R.id.Cartcount);
//
//        if(count==0)
//            tv.setVisibility(View.GONE);
//       else
//            tv.setText(""+count);
        return super.onCreateOptionsMenu(menu);
    }

//    public void ExitDialog() {
//       MyAlert myalert = new MyAlert();
//        myalert.show(getFragmentManager(), "Myalert");
//
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

        }
        return super.onOptionsItemSelected(item);

    }

    protected void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
    }

    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

       public void showToolbar()
    {
         toolbar = (Toolbar) findViewById(R.id.app_bar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setHomeButtonEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    public void loadDrawer()
    {
        NavDrawrFragment drawerFrag=(NavDrawrFragment)getSupportFragmentManager().findFragmentById(R.id.navdrawer);
        drawerFrag.setup(R.id.navdrawer,(android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout),toolbar);
    }

    public void setCartcount(int count)
    {
        if(count==0)
        {
            tv.setVisibility(View.GONE);
        }
        else
            tv.setText(""+count);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                //.setIcon(R.drawable.tick)
//              .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }


}