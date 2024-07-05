package com.example.weatherapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCli {

    //OkHttp Logging
//    var mHttpLoggingInterceptor = HttpLoggingInterceptor()
//        .setLevel(HttpLoggingInterceptor.Level.BODY)
//
//    var mOkHttpClient = OkHttpClient
//        .Builder()
//        .addInterceptor(mHttpLoggingInterceptor)
//        .build()


//    var mRetrofit: Retrofit? = null

    //Singleton instance of retrofit
//    val client: Retrofit?
//        get(){
//            if (mRetrofit == null){
//                mRetrofit = Retrofit.Builder()
//                    .baseUrl("http://api.openweathermap.org/")
//                    .client(mOkHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            }
//            return mRetrofit
//        }



}