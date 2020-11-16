package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service.model.OAuthAccessTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface BlizzardAPI {

    @FormUrlEncoded
    @POST("/oauth/token")
    fun AccessTokenRequest(
        @Header("Authorization") authorizationHeader: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String
    ): Call<OAuthAccessTokenResponse>
}