package com.example.myopeninapp.repository

import com.example.myopeninapp.data.api.ApiClient

class LinksRepository {
    suspend fun getLinksData() = ApiClient.api.getLinksData()
}



