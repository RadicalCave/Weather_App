package com.example.weatherapp.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.api.ApiHelper
import com.example.weatherapp.api.ApiHelperImpl
import com.example.weatherapp.api.WeatherAPI
import com.example.weatherapp.repo.DataStorePreferencesRepo
import com.example.weatherapp.repo.DataStorePreferencesRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else OkHttpClient.Builder().build()


    @Provides
    @Singleton
    fun provideRetrofitCli(okHttpClient: OkHttpClient,BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(WeatherAPI::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper


    private const val LOCATION_COORDS = "location-coordinates"

    @Module
    @InstallIn(SingletonComponent::class)
    object DataStoreModule{
        @Provides
        @Singleton
        fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences>{
            return PreferenceDataStoreFactory.create(
                produceFile = {context.preferencesDataStoreFile(LOCATION_COORDS)}
            )
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface DataStoreRepoModule {
        @Binds
        fun provideDataStoreRepoImpl(repoImpl: DataStorePreferencesRepoImpl): DataStorePreferencesRepo
    }

}