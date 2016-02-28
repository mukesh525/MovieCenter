package muk.materialdesign.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import muk.materialdesign.R;
import muk.materialdesign.network.VolleySingleTon;

/**
 * Created by Mukesh on 4/14/2015.
 */
public class Myfragment extends Fragment {
    private TextView textview;

    public static Myfragment getInstance(int position) {
        Myfragment myfragment = new Myfragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myfragment.setArguments(args);
        return myfragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.my_fragment, container, false);
        textview = (TextView) layout.findViewById(R.id.position);
        Bundle args = getArguments();
        if (args != null) {
            textview.setText("The Page Selected is " + args.getInt("position"));
        }
        //RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        RequestQueue requestQueue= VolleySingleTon.getInstance().getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.GET,"https://www.google.co.in/",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getActivity(),"Response"+response,Toast.LENGTH_LONG).show();

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(getActivity(),"Error"+error,Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);

        return layout;
    }
}
