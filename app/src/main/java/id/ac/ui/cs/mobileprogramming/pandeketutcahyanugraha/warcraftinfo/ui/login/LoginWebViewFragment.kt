package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.LoginWebViewFragmentBinding

class LoginWebViewFragment : Fragment() {

    companion object {
        fun newInstance() =
            LoginWebViewFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var _binding: LoginWebViewFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginWebViewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}