package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.LoginActivityBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.service.AuthService

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var authService: AuthService
    private var authServiceBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AuthService.LoginServiceBinder
            authService = binder.getService()
            authServiceBound = true

            val action: String? = intent?.action
            val data: Uri? = intent?.data

            if (action == Intent.ACTION_VIEW && authServiceBound) {
                val accessToken = data?.getQueryParameter("access_token")
                val errorStatusCode = data?.getQueryParameter("error_status_code")
                if (accessToken != null) {
                    authService.saveAccessToken(accessToken)
                    // TODO Start Activity with Result
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.toast_login_failed).plus(errorStatusCode),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            authServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonLogin.setOnClickListener {
            val authorizationCodeRequestURL = StringBuilder().append(WarcraftInfoConstant.BASE_API_URI)
                .append(WarcraftInfoConstant.AUTHORIZATION_CODE_REQUEST_PATH)
                .append("?client_id=").append(WarcraftInfoConstant.API_CLIENT_ID)
                .append("&scope=").append(WarcraftInfoConstant.WOW_PROFILE_SCOPE)
                .append("&redirect_uri=").append(WarcraftInfoConstant.OAUTH_REDIRECT_URI)
                .append("&response_type=").append(WarcraftInfoConstant.AUTHORIZATION_CODE_REQUEST_RESPONSE_TYPE)
                .toString()
            val webpage: Uri = Uri.parse(authorizationCodeRequestURL)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, AuthService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        authServiceBound = false
    }
}