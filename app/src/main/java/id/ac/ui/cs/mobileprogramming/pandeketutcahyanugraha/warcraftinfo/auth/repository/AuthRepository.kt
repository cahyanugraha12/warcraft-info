package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.BlizzardAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.TaranzhiAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val blizzardAPI: BlizzardAPI,
    private val taranzhiAPI: TaranzhiAPI,
    @ApplicationContext private val appContext: Context
) {
    fun getStaticToken(reset: Boolean): String? {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        var staticToken = sharedPreferences.getString(WarcraftInfoConstant.STATIC_TOKEN_KEY, null)
        if (staticToken == null || reset) {
            val requestAccessToken = taranzhiAPI.getStaticToken().execute()
            val requestAccessTokenResponse = requestAccessToken.body() ?: return null
            if (requestAccessTokenResponse.code != 200) {
                return null
            }

            saveStaticToken(requestAccessTokenResponse.staticToken)
            staticToken = requestAccessTokenResponse.staticToken
        }
        return staticToken
    }

    private fun saveStaticToken(staticToken: String) {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        sharedPreferences
            .edit()
            .putString(WarcraftInfoConstant.STATIC_TOKEN_KEY, staticToken)
            .apply()
    }

    fun getAccessToken(): String? {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(WarcraftInfoConstant.ACCESS_TOKEN_KEY, null)
    }

    fun saveAccessToken(accessToken: String) {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        sharedPreferences
            .edit()
            .putString(WarcraftInfoConstant.ACCESS_TOKEN_KEY, accessToken)
            .apply()
    }

    fun getUseDummyDataFlag(): Boolean {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(WarcraftInfoConstant.USE_DUMMY_DATA, false)
    }

    fun setUseDummyDataFlag(value: Boolean) {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        sharedPreferences
            .edit()
            .putBoolean(WarcraftInfoConstant.USE_DUMMY_DATA, value)
            .apply()
    }

}
