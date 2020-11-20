package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository.AuthRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.LoginActivityBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonLogin.setOnClickListener {
            val authorizationCodeRequestURL = StringBuilder().append(WarcraftInfoConstant.BASE_AUTH_API_URI)
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

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        if (action == Intent.ACTION_VIEW) {
            val accessToken = data?.getQueryParameter("access_token")
            val errorStatusCode = data?.getQueryParameter("error_status_code")
            if (accessToken != null) {
                authRepository.saveAccessToken(accessToken)
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.toast_login_failed).plus(errorStatusCode),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}