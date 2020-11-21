package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class CharacterMediaSummaryAPIDao(
    // US-Oceania
    val assets: List<GenericKeyValue>?,
    // US-North America
    @SerializedName ("avatar_url") val avatarUrl: String?,
    @SerializedName ("render_url") val mainUrl: String?
)