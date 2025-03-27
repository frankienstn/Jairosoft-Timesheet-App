package com.example.jairosofttimesheet.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://timesheet-63231.bubbleapps.io/api/1.1/wf/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.client(okhttpclien
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        //.create(ApiService::class.java)
        .build()


    //delete this
    val apiService: ApiService = retrofit.create(ApiService::class.java)
//    val attendanceService: AttendanceApiService = retrofit.create(AttendanceApiService::class.java)
}

//OkhttpClient  dependency