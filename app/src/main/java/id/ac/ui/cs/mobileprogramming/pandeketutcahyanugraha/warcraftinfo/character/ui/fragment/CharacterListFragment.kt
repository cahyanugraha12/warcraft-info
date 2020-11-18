package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CharacterListFragmentBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel.CharacterViewModel

@AndroidEntryPoint
class CharacterListFragment : Fragment() {

    companion object {
        fun newInstance() = CharacterListFragment()
    }

    private var _binding: CharacterListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel: CharacterViewModel by activityViewModels()
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.containerLoadingCharacterList.visibility = View.VISIBLE
                binding.containerDataCharacterList.visibility = View.GONE
            } else {
                // TODO Further distinguish between actual loading error and access token null/expired
                if (viewModel.isSuccess) {
                    binding.containerLoadingCharacterList.visibility = View.GONE
                    binding.containerDataCharacterList.visibility = View.VISIBLE
                } else {
                    binding.containerLoadingCharacterList.visibility = View.VISIBLE
                    binding.containerDataCharacterList.visibility = View.GONE
                    binding.messageLoadingCharacterList.text = getString(R.string.message_data_loading_failed)
                }
            }
        })
        viewModel.characterSummaryList.observe(viewLifecycleOwner, Observer { characterSummaryList ->
            if (characterSummaryList.isEmpty()) {
                viewModel.isSuccess = false
            } else {
                binding.recyclerViewCharacterList.layoutManager = activity?.let {
                    LinearLayoutManager(it)
                }
                binding.recyclerViewCharacterList.adapter = activity?.let {
                    CharacterSummaryListAdapter(
                        it,
                        characterSummaryList,
                        viewModel,
                        callback
                    )
                }
            }
        })
    }

    // Callback for activity to communicate about item clicked event
    private lateinit var callback: OnCharacterSelectedListener

    fun setOnCharacterSelectedListener(callback: OnCharacterSelectedListener) {
        this.callback = callback
    }

    interface OnCharacterSelectedListener {
        fun onCharacterSelected()
    }

    // Adapter for RecycleView
    class CharacterSummaryListAdapter(
        val context: Context,
        val characterSummaryList: List<CharacterSummary>,
        val viewModel: CharacterViewModel,
        val callback: OnCharacterSelectedListener
    ) : RecyclerView.Adapter<CharacterSummaryListAdapter.CharacterSummaryListViewHolder>() {
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val position = v.tag as Int
                viewModel.currentCharacterSummarySelectedPosition.value = position
                callback.onCharacterSelected()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterSummaryListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_list_item, parent, false)
            return CharacterSummaryListViewHolder(view)
        }

        override fun onBindViewHolder(holder: CharacterSummaryListViewHolder, position: Int) {
            val item = characterSummaryList[position]
            if (item.avatarMediaLink != "") {
                Picasso
                    .get()
                    .load(item.avatarMediaLink)
                    .resize(60, 60)
                    .into(holder.characterAvatar)
            }
            holder.characterName.text = context.getString(
                R.string.label_character_list_character_name,
                item.name,
                item.level,
                item.faction,
                item.gender,
                item.race,
                item.characterClass,
                item.realm
            )

            with(holder.itemView) {
                tag = position
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = characterSummaryList.size

        inner class CharacterSummaryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val characterAvatar: ImageView = view.findViewById(R.id.image_view_character_avatar)
            val characterName: TextView = view.findViewById(R.id.text_view_character_name)
        }
    }
}