package com.legocatalog.di

import com.legocatalog.BuildConfig
import com.legocatalog.data.remote.rebrickable.RebrickableService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    fun providesRebrickableService(retrofit: Retrofit) = retrofit.create(RebrickableService::class.java) as RebrickableService

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient) =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.REBRICKABLE_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

    @Provides
    @Singleton
    fun provideHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideauthInterceptor() = AuthInterceptor()

    class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val newRequest = request
                    .newBuilder()
                    .header("Authorization", BuildConfig.REBRICKABLE_AUTH_TOKEN)
                    .method(request.method(), request.body())
                    .build()
            return chain.proceed(newRequest)
        }
    }
}