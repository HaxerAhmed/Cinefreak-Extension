package com.example.cinefreak

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils
import org.jsoup.nodes.Element

class CinefreakProvider : MainAPI() {
    override var mainUrl = "https://cinefreak.net"
    override var name = "Cinefreak"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie)

    override suspend fun search(query: String, page: Int): List<SearchResponse> {
        val url = "$mainUrl/?s=${AppUtils.encodeURLParameter(query)}"
        val document = app.get(url).document
        return document.select("h2.entry-title a").map {
            val title = it.text()
            val href = it.attr("href")
            MovieSearchResponse(
                title = title,
                url = href,
                apiName = this.name,
                type = TvType.Movie
            )
        }
    }

    // TODO: implement main page and load functions
}
