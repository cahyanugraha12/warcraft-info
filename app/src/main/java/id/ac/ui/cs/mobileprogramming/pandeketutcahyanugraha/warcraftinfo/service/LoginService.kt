package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R


class LoginService : Service() {

    private val binder = LoginServiceBinder()

    fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(getString(R.string.access_token_key), null)
    }

    fun saveAccessToken(accessToken: String) {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        sharedPreferences
            .edit()
            .putString(getString(R.string.access_token_key), accessToken)
            .apply()
    }

    inner class LoginServiceBinder: Binder() {
        fun getService(): LoginService = this@LoginService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}
