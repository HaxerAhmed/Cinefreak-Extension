package com.example

import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MovieSearchResponse
import com.lagradost.cloudstream3.MovieLoadResponse
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.utils.loadExtractor

class PutlockersProvider : MainAPI() {
    override var mainUrl = "https://putlockers.vg"
    override var name = "Putlockers"
    override val hasMainPage = false
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun search(query: String, page: Int): List<SearchResponse> {
        val url = "$mainUrl/search?q=${query.encodeURLParameter()}"
        val document = app.get(url).document
        return document.select("div.item").mapNotNull { element ->
            val link = element.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val title = element.selectFirst("a")?.attr("title") ?: return@mapNotNull null
            val poster = element.selectFirst("img")?.attr("src")
            MovieSearchResponse(
                title = title,
                url = fixUrl(link),
                apiName = name,
                type = TvType.Movie,
                posterUrl = fixUrl(poster)
            )
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text() ?: return null
        val poster = doc.selectFirst("div.poster img")?.attr("src")
        val description = doc.selectFirst("div#plot")?.text()
        val watchLink = doc.selectFirst("a.btn")?.attr("href") ?: return null
        return MovieLoadResponse(
            title = title,
            url = url,
            apiName = name,
            type = TvType.Movie,
            dataUrl = fixUrl(watchLink),
            posterUrl = fixUrl(poster),
            year = null,
            plot = description
        )
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean): Boolean {
        val doc = app.get(data).document
        val iframe = doc.selectFirst("iframe")?.attr("src") ?: return false
        return loadExtractor(iframe, name, data, subtitleCallback, callback)
    }
}
