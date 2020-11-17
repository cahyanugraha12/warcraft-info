package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service.dao.AccountProfileSummary
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharacterService : Service() {

    private lateinit var blizzardAPI: BlizzardAPI
    private val binder = ProfileServiceBinder()

    fun getProfileSummary(onResult: (AccountProfileSummary?, Int) -> Unit) {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        val accessToken = sharedPreferences.getString(getString(R.string.access_token_key), null)
            ?: return onResult(null, 401)

        blizzardAPI.getProfileSummary(
            WarcraftInfoConstant.NAMESPACE_PROFILE,
            WarcraftInfoConstant.LOCALE,
            accessToken
        ).enqueue(object : Callback<AccountProfileSummary> {
            override fun onResponse(
                call: Call<AccountProfileSummary>,
                response: Response<AccountProfileSummary>
            ) {
                return if (response.isSuccessful) {
                    onResult(response.body(), 200)
                } else {
                    onResult(null, response.code())
                }
            }

            override fun onFailure(call: Call<AccountProfileSummary>, t: Throwable) {
                return onResult(null, 400)
            }
        })
    }

    inner class ProfileServiceBinder: Binder() {
        fun getService(): CharacterService = this@CharacterService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl(WarcraftInfoConstant.BASE_API_URI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        blizzardAPI = retrofit.create(BlizzardAPI::class.java)
    }
}
