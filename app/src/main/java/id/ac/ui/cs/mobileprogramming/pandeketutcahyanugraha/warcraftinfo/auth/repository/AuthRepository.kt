package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.BlizzardAPI
import javax.inject.Inject


class AuthRepository @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    fun getAccessToken(): String? {
        val sharedPreferences = appContext.getSharedPreferences(
            appContext.getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(appContext.getString(R.string.access_token_key), null)
    }

    fun saveAccessToken(accessToken: String) {
        val sharedPreferences = appContext.getSharedPreferences(
            appContext.getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        sharedPreferences
            .edit()
            .putString(appContext.getString(R.string.access_token_key), accessToken)
            .apply()
    }
}
