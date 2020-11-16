package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service.model.OAuthAccessTokenResponse
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginService : Service() {

    private lateinit var blizzardAPI: BlizzardAPI
    private val binder = LoginServiceBinder()

    fun requestAccessToken(authorizationCode: String, onResult: (OAuthAccessTokenResponse?) -> Unit) {
        var authorizationHeader = Credentials.basic(
            WarcraftInfoConstant.API_CLIENT_ID,
            WarcraftInfoConstant.API_CLIENT_SECRET
        )
        blizzardAPI.AccessTokenRequest(
            authorizationHeader,
            WarcraftInfoConstant.OAUTH_REDIRECT_URI,
            WarcraftInfoConstant.WOW_PROFILE_SCOPE,
            WarcraftInfoConstant.ACCESS_TOKEN_GRANT_TYPE,
            authorizationCode
        ).enqueue(object : Callback<OAuthAccessTokenResponse> {
            override fun onResponse(
                call: Call<OAuthAccessTokenResponse>,
                response: Response<OAuthAccessTokenResponse>
            ) {
                if (response.isSuccessful) {
                    return onResult(response.body())
                } else {
                    return onResult(null)
                }
            }

            override fun onFailure(call: Call<OAuthAccessTokenResponse>, t: Throwable) {
                return onResult(null)
            }
        })
    }

    inner class LoginServiceBinder: Binder() {
        fun getService(): LoginService = this@LoginService
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
