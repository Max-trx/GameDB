package com.example.gamedatabase

import android.app.Application
import com.example.gamedatabase.data.AppContainer
import com.example.gamedatabase.data.DefaultAppContainer

class RawgApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
