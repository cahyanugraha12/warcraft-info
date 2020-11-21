package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository.AuthRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.ui.LoginActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment.CharacterDetailFragment
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment.CharacterListFragment
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel.CharacterViewModel
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
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
        val useDataDummyFlag = authRepository.getUseDummyDataFlag()
        if (accessToken == null && !useDataDummyFlag) {
            val intent = Intent(this@CharacterActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityForResult(intent, WarcraftInfoConstant.START_ACTIVITY_FOR_RESULT_REQUEST_CODE_LOGIN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WarcraftInfoConstant.START_ACTIVITY_FOR_RESULT_REQUEST_CODE_LOGIN) {
            val viewModel: CharacterViewModel by viewModels()
            viewModel.getCharacterSummary()
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

    override fun onDestroy() {
        authRepository.setUseDummyDataFlag(false)
        super.onDestroy()
    }
}