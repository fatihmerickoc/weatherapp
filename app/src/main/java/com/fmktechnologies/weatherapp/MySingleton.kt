package com.fmktechnologies.weatherapp

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import androidx.core.view.ViewCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

class MySingleton private constructor(private val context:Context) {


    private var requestQueue: RequestQueue?




    init {
        requestQueue = getRequestQueue()

    }


    fun <T> addToRequestQueue(req: Request<T>?) {
        getRequestQueue()!!.add(req)
    }






    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) { // getApplicationContext() is key, it keeps you from leaking the
// Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.applicationContext)
        }
        return requestQueue
    }








    companion object {

        private var instance: MySingleton? = null

        @Synchronized

        fun getInstance(context: Context): MySingleton? {
            if (instance == null) {

                instance = MySingleton(context)

            }


            return instance
        }




    }








}