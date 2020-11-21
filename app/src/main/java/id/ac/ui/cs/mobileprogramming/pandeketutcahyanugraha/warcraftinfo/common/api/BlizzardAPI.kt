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
    ): Call<AccountProfileSummaryAPIDao>

    @GET("/profile/wow/character/{realmSlug}/{characterName}/equipment")
    fun getCharacterEquipment(
        @Path("realmSlug") realmSlug: String,
        @Path("characterName") characterNameLowercase: String,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<CharacterEquipmentSummaryAPIDao>

    @GET("/profile/wow/character/{realmSlug}/{characterName}/character-media")
    fun getCharacterMedia(
        @Path("realmSlug") realmSlug: String,
        @Path("characterName") characterNameLowercase: String,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<CharacterMediaSummaryAPIDao>

    // TODO Hardcoded to Nagrand for now
    @GET("/data/wow/connected-realm/3721/auctions")
    fun getAuctionHouseData(
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<AuctionHouseAPIDao>

    @GET("/data/wow/item-class/index")
    fun getItemClassesIndex(
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<ItemClassesIndexAPIDao>

    @GET("data/wow/item/{itemId}")
    fun getItemById(
        @Path("itemId") itemId: Int,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<ItemAPIDao>

    @GET("data/wow/media/item/{itemId}")
    fun getItemMediaById(
        @Path("itemId") itemId: Int,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<ItemMediaAPIDao>
}