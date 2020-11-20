package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.*
import retrofit2.Call
import retrofit2.http.*

interface BlizzardAPI {

    @GET("/profile/user/wow")
    fun getProfileSummary(
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<AccountProfileSummary>

    @GET("/profile/wow/character/{realmSlug}/{characterName}/equipment")
    fun getCharacterEquipment(
        @Path("realmSlug") realmSlug: String,
        @Path("characterName") characterNameLowercase: String,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<CharacterEquipmentSummary>

    @GET("/profile/wow/character/{realmSlug}/{characterName}/character-media")
    fun getCharacterMedia(
        @Path("realmSlug") realmSlug: String,
        @Path("characterName") characterNameLowercase: String,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<CharacterMediaSummary>

    @GET("/data/wow/item-class/index")
    fun getItemClassesIndex(
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<ItemClassesIndex>

    @GET("data/wow/item/{itemId}")
    fun getItemById(
        @Path("itemId") itemId: Int,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<Item>

    @GET("data/wow/media/item/{itemId}")
    fun getItemMediaById(
        @Path("itemId") itemId: Int,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<ItemMedia>
}