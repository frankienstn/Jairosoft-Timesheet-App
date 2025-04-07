package com.example.jairosofttimesheet.data.remote

import com.example.jairosofttimesheet.data.auth.AttendanceApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://timesheet-63231.bubbleapps.io/api/1.1/wf/"
private const val AUTH_TOKEN = "bus|1741329443584x643073812085500400|1743400412014x556387795990054300"

// Moshi Adapter
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// OkHttpClient with Interceptor for adding Authorization header
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $AUTH_TOKEN")
            .build()
        chain.proceed(request)
    }
    .build()

// Retrofit Instance
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)  // Attach the OkHttpClient to Retrofit
    .build()

object RetrofitClient {

    val attendanceApiService: AttendanceApiService = retrofit.create(AttendanceApiService::class.java)
    val authApiService: AuthApiService = retrofit.create(AuthApiService::class.java)
}
