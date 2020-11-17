package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.service.AuthService
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.service.CharacterService
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment.CharacterListFragment
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CharacterActivityBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel.CharacterViewModel

class CharacterActivity : AppCompatActivity() {

    private lateinit var binding: CharacterActivityBinding
    private lateinit var viewModel: CharacterViewModel
    var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CharacterActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)

        if (binding.fragmentOuterContainerCharacterDetail != null) {
            twoPane = true
        }

        if (twoPane) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentOuterContainerCharacterList!!.id, CharacterListFragment())
                .replace(binding.fragmentOuterContainerCharacterDetail!!.id, CharacterListFragment())
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerCharacterActivity!!.id, CharacterListFragment())
                .commit()
        }
    }
}