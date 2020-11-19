package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.service.AuthService
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.ui.LoginActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment.CharacterDetailFragment
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment.CharacterListFragment
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CharacterActivityBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel.CharacterViewModel

@AndroidEntryPoint
class CharacterActivity : AppCompatActivity(), CharacterListFragment.OnCharacterSelectedListener {

    private lateinit var binding: CharacterActivityBinding
    var twoPane: Boolean = false
    private lateinit var authService: AuthService
    private var authServiceBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AuthService.LoginServiceBinder
            authService = binder.getService()
            authServiceBound = true

            val accessToken = authService.getAccessToken()
            if (accessToken == null) {
                val intent = Intent(this@CharacterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            authServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CharacterActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel: CharacterViewModel by viewModels()

        if (binding.fragmentOuterContainerCharacterDetail != null) {
            twoPane = true
        }

        if (twoPane) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentOuterContainerCharacterList!!.id, CharacterListFragment.newInstance())
                .replace(binding.fragmentOuterContainerCharacterDetail!!.id, CharacterDetailFragment.newInstance())
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerCharacterActivity.id, CharacterListFragment.newInstance())
                .commit()
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

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is CharacterListFragment) {
            fragment.setOnCharacterSelectedListener(this)
        }
    }

    override fun onCharacterSelected() {
        if (!twoPane) {
            supportFragmentManager.beginTransaction()
                .replace(binding.containerCharacterActivity.id, CharacterDetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}