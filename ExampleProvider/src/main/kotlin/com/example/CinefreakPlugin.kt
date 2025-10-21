package com.example

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class CinefreakPlugin : Plugin() {
    override fun load(context: Context) {
        // Register the Cinefreak provider so that CloudStream recognizes it
        registerMainAPI(CinefreakProvider())
    }
}
