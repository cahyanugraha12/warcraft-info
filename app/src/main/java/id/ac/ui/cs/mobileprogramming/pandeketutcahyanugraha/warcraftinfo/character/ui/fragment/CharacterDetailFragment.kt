package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel.CharacterViewModel
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CharacterDetailFragmentBinding

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {

    companion object {
        fun newInstance() = CharacterDetailFragment()
    }

    private var _binding: CharacterDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel: CharacterViewModel by activityViewModels()
//        viewModel.currentCharacterSummarySelectedId.observe(viewLifecycleOwner, Observer<Int?> { id ->
//            if (id != null) {
//                binding.containerLoadingCharacterDetail.visibility = View.GONE
//                binding.containerDataCharacterDetail.visibility = View.VISIBLE
//            }
//        })
    }

}