package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CharacterListFragmentBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel.CharacterViewModel

class CharacterListFragment : Fragment() {

    companion object {
        fun newInstance() = CharacterListFragment()
    }

    private var _binding: CharacterListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CharacterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(CharacterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}