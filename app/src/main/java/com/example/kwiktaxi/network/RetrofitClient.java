package com.example.kwiktaxi.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//199.168.6.108
public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.0.173:5000/"; // Flask server URL
    private static RetrofitClient instance;
    private Retrofit retrofit;

    private RetrofitClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

    public RankDestinationApi getRankDestinationApi() {
        return retrofit.create(RankDestinationApi.class);
    }

    public TaxiApi getTaxiApi() {
        return retrofit.create(TaxiApi.class);
    }

    public DriverApi getDriverApi() {
        return retrofit.create(DriverApi.class);
    }
}
