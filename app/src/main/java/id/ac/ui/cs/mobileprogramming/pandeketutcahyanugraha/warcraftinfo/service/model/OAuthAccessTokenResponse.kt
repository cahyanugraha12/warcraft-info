package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service.model;

import com.google.gson.annotations.SerializedName

data class OAuthAccessTokenResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in") val expiresIn: Int,
        val scope: String
        )
