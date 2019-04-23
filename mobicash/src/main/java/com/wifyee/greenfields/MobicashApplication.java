package com.wifyee.greenfields;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.wifyee.greenfields.Utils.LruBitmapCache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

public class MobicashApplication extends Application {
    /*
   * Cache for holding application wide images.
   */
    private LruBitmapCache cache;
    public LruBitmapCache getLruBitmapCache() {
        return cache;
    }
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private FirebaseRemoteConfig firebaseRemoteConfig=null;
    /*
    * App instance and is single instance per app.
    */
    private static MobicashApplication singleton;
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        context = this;
        cache = new LruBitmapCache();
        Timber.plant(new Timber.DebugTree());
        Timber.d("Planted Debug Tree!");
        try {
            FirebaseApp.initializeApp(context);
            //FirebaseRemoteConfig.zzcy(context);
            //firebaseRemoteConfig.
            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        }
       catch (Exception ex) {
           ex.printStackTrace();
       }
        /*Map<String,Object> stringObjectMap=new HashMap<String, Object>();
        stringObjectMap.put(UpdateHelper.KEY_UPDATE_REQUIRED, false);
        stringObjectMap.put(UpdateHelper.KEY_CURRENT_VERSION, "1.1.29");
        stringObjectMap.put(UpdateHelper.KEY_UPDATE_URL,"https://play.google.com/store/apps/details?id=com.wifyee.greenfields");
        firebaseRemoteConfig.setDefaults(stringObjectMap);
        firebaseRemoteConfig.fetch(60).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    firebaseRemoteConfig.activateFetched();
                }
            }
        });*/

       /* AndroidNetworking.initialize(getApplicationContext());
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request();
                        // Show API Log...
                        long t1 = System.nanoTime();
                        Timber.d(String.format("Sending request %s on %s%n%s",
                                newRequest.url(), chain.connection(), newRequest.headers()));
                        Timber.d("METHOD TYPE ==>> " + newRequest.method().toString());
                        if (newRequest.body() != null) {
                            Timber.d("BODY: " + requestBodyToString(newRequest.body()));
                        }
                        Response response = chain.proceed(newRequest);
                        long t2 = System.nanoTime();
                        Timber.d(String.format(Locale.US, "Received response for %s in %.1fms%n%s",
                                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
                        if (response.body() != null) {
                            ResponseBody jsonObj = response.body();
                            if (jsonObj != null) {
                                Timber.d("BODY: " + jsonObj.toString());
                            }
                        }
                        return response;
                    }
                })
                .build();

        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        AndroidNetworking.enableLogging();*/
    }

    /**
     * Get the instance of MobicashApplication.
     */
    public static MobicashApplication getInstance() {
        return singleton;
    }
    /**
     * string representation of request body
     *
     * @param request
     * @return
     */
    public static String requestBodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        } catch (Exception | OutOfMemoryError e) {
            return e.getMessage();
        }
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            if (cache == null) cache = new LruBitmapCache();
            imageLoader = new ImageLoader(this.requestQueue,
                    cache);
        }
        return this.imageLoader;
    }
}
