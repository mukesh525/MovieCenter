package muk.materialdesign.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import muk.materialdesign.Utils.MyApplication;

/**
 * Created by Mukesh on 4/14/2015.
 */
public class VolleySingleTon {
    public static VolleySingleTon sInstance=null;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private VolleySingleTon()
    {
            requestQueue= Volley.newRequestQueue(MyApplication.getAplicationContext());
            imageLoader=new ImageLoader(requestQueue,new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap> cache=new LruCache<>(((int)(Runtime.getRuntime().maxMemory()/1024)/8));
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
              cache.put(url,bitmap);
            }
        });

    }
    public static VolleySingleTon getInstance()
    {
        if(sInstance==null)
        {
            sInstance=new   VolleySingleTon();
        }
        return sInstance;

    }

    public RequestQueue getRequestQueue()
    {
        return requestQueue;
    }
    public ImageLoader getImageLoader()
    {
        return imageLoader;
    }
}
