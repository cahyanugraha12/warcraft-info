package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AccountProfileSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.CharacterMediaSummary
import retrofit2.Call
import retrofit2.http.*

interface BlizzardAPI {

    @GET("/profile/user/wow")
    fun getProfileSummary(
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<AccountProfileSummary>

    @GET("/profile/wow/character/{realmSlug}/{characterName}/character-media")
    fun getCharacterMedia(
        @Path("realmSlug") realmSlug: String,
        @Path("characterName") characterNameLowercase: String,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<CharacterMediaSummary>
}