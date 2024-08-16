package com.example.myopeninapp.repository

import com.example.myopeninapp.data.api.ApiClient

class DashboardRepository {
    suspend fun getDashboardData() = ApiClient.api.getDashboardData()
}




