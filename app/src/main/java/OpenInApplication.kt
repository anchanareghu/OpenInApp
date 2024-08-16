package com.example.myopeninapp

import android.app.Application
import android.content.Context

class OpenInApplication : Application() {
    companion object {
        var appContext: Context? = null
    }
}