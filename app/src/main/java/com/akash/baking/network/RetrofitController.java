package com.akash.baking.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class RetrofitController {

    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static final String TAG = RetrofitController.class.getSimpleName();
    private static final int OKHTTP_CLIENT_TIMEOUT = 30;
    private static RetrofitController retrofitInstance;
    private JacksonConverterFactory jacksonConverterFactory;

    private BakingAPI bakingAPI;

    private RetrofitController() {
        Log.v(TAG, " in RetrofitController()");

        //Make JSON converter
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jacksonConverterFactory = JacksonConverterFactory.create(objectMapper);
    }

    public static RetrofitController getInstance() {
        Log.v(TAG, RetrofitController.class.getName() + " in getInstance()");

        if (retrofitInstance == null) {
            synchronized (RetrofitController.class) {
                if (retrofitInstance == null) {
                    retrofitInstance = new RetrofitController();
                }
            }
        }
        return retrofitInstance;
    }

    @NonNull
    private retrofit2.Retrofit getRetrofitBuilder(){
        OkHttpClient okHttpClient;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);

        //Make http client
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(OKHTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(OKHTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .dispatcher(dispatcher)
                .followRedirects(false)
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RetrofitController.BASE_URL)
                .addConverterFactory(jacksonConverterFactory)
                .build();
    }

    public BakingAPI getBakingAPI(){

        if(bakingAPI!=null){
            return bakingAPI;
        }

        Retrofit retrofit = getRetrofitBuilder();
        bakingAPI = retrofit.create(BakingAPI.class);

        return bakingAPI;
    }
}
