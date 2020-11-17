package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AccountProfileSummary
import retrofit2.Call
import retrofit2.http.*

interface BlizzardAPI {

    @GET("/profile/user/wow")
    fun getProfileSummary(
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): Call<AccountProfileSummary>
}