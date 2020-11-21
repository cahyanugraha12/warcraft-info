package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class StaticTokenAPIDao(
    @SerializedName("static_token") val staticToken: String,
    val code: Int
)