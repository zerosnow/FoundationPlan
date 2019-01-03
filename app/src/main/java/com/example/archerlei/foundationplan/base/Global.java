package com.example.archerlei.foundationplan.base;

import android.content.Context;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by archerlei on 2017/7/11.
 */

public class Global {


    public final static Singleton<RequestQueue, Context> mRequestQueue = new Singleton<RequestQueue, Context>() {
        @Override
        protected RequestQueue create(Context context) {
            RequestQueue queue;
            Cache cache = new DiskBasedCache(context.getCacheDir());

            Network network = new BasicNetwork(new HurlStack());

            queue = new RequestQueue(cache, network);

            queue.start();
            return queue;
        }
    };

}
