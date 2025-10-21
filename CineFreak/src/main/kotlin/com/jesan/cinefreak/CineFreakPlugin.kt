package com.jesan.cinefreak

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class CineFreakPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(CineFreakProvider())
    }
}
