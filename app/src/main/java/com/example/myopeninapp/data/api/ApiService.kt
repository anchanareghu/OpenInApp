package com.example.myopeninapp.data.api

import com.example.myopeninapp.com.example.myopeninapp.data.model.data.DashboardData
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("api/v1/dashboardNew")
    suspend fun getDashboardData(): Response<DashboardData>

    @POST("api/v1/logout")
    suspend fun postData(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): Response<Any>
}


