package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AccountProfileSummaryAPIDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.CharacterEquipmentSummaryAPIDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.CharacterMediaSummaryAPIDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.StaticTokenAPIDao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TaranzhiAPI {

    @GET("/api/request-static-token")
    fun getStaticToken(): Call<StaticTokenAPIDao>

    @GET("/api/character-summary")
    fun getCharacterSummary(): Call<AccountProfileSummaryAPIDao>

    @GET("/api/character-equipment/{realmSlug}/{characterName}")
    fun getCharacterEquipment(
        @Path("realmSlug") realmSlug: String,
        @Path("characterName") characterNameLowercase: String
    ): Call<CharacterEquipmentSummaryAPIDao>

    @GET("/api/character-media/{realmSlug}/{characterName}")
    fun getCharacterMedia(
        @Path("realmSlug") realmSlug: String,
        @Path("characterName") characterNameLowercase: String
    ): Call<CharacterMediaSummaryAPIDao>
}