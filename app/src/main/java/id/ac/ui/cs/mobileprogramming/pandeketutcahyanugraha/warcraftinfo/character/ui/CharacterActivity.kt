package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository.AuthRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.ui.LoginActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment.CharacterDetailFragment
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment.CharacterListFragment
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CharacterActivityBinding
import javax.inject.Inject

@AndroidEntryPoint
class CharacterActivity : AppCompatActivity(), CharacterListFragment.OnCharacterSelectedListener {

    private lateinit var binding: CharacterActivityBinding
    var twoPane: Boolean = false

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CharacterActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val accessToken = authRepository.getAccessToken()
        if (accessToken == null) {
            val intent = Intent(this@CharacterActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        twoPane = binding.fragmentOuterContainerCharacterDetail != null

        if (twoPane) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentOuterContainerCharacterList!!.id, CharacterListFragment.newInstance())
                .replace(binding.fragmentOuterContainerCharacterDetail!!.id, CharacterDetailFragment.newInstance())
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentOuterContainerCharacterActivity!!.id, CharacterListFragment.newInstance())
                .commit()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is CharacterListFragment) {
            fragment.setOnCharacterSelectedListener(this)
        }
    }

    override fun onCharacterSelected() {
        if (!twoPane) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentOuterContainerCharacterActivity!!.id, CharacterDetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}