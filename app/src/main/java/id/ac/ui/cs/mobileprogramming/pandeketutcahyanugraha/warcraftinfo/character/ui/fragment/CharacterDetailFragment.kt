package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.ui.LoginActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel.CharacterViewModel
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CharacterDetailFragmentBinding
import java.lang.Exception

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
        viewModel.characterDetailLoadingStatus.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.containerLoadingCharacterDetail.visibility = View.VISIBLE
                binding.containerDataCharacterDetail.visibility = View.GONE
            } else {
                if (viewModel.characterDetailLoadingStatus.isSuccess) {
                    binding.containerLoadingCharacterDetail.visibility = View.GONE
                    binding.containerDataCharacterDetail.visibility = View.VISIBLE
                } else if (viewModel.characterDetailLoadingStatus.errorCode == WarcraftInfoConstant.ACCESS_TOKEN_INVALID){
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                } else {
                    binding.containerLoadingCharacterDetail.visibility = View.VISIBLE
                    binding.containerDataCharacterDetail.visibility = View.GONE
                    binding.messageLoadingCharacterDetail.text = getString(R.string.message_data_loading_failed)
                }
            }
        })
        viewModel.currentCharacterSummarySelectedPosition.observe(viewLifecycleOwner, Observer { id ->
            if (id != null) {
                viewModel.getCharacterItemList()
                val currentSelectedCharacterSummary = viewModel.characterSummaryList.value?.get(
                    id
                )
                Picasso
                    .get()
                    .load(currentSelectedCharacterSummary?.mainMediaLink)
                    .into(binding.imageViewCharacterMainMedia)
                binding.buttonSaveCharacterMedia.setOnClickListener {
                    val target: Target = object : Target {
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            return
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                            Toast.makeText(
                                activity,
                                getString(R.string.text_failed_save_character_image_to_gallery),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                            MediaStore.Images.Media.insertImage(
                                activity?.contentResolver,
                                bitmap,
                                getString(
                                    R.string.template_character_image_name,
                                    currentSelectedCharacterSummary!!.name,
                                    System.currentTimeMillis()
                                ),
                                getString(
                                    R.string.template_character_image_description,
                                    currentSelectedCharacterSummary.name
                                )
                            )
                            Toast.makeText(
                                activity,
                                getString(R.string.text_success_save_character_image_to_gallery),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    Picasso
                        .get()
                        .load(currentSelectedCharacterSummary?.mainMediaLink)
                        .into(target)
                }
            }
        })

        val slotAndViewTripleList = listOf(
            Triple(WarcraftInfoConstant.SLOT_HEAD, R.id.image_view_item_head, R.id.text_view_item_head),
            Triple(WarcraftInfoConstant.SLOT_NECK, R.id.image_view_item_neck, R.id.text_view_item_neck),
            Triple(WarcraftInfoConstant.SLOT_SHOULDER, R.id.image_view_item_shoulder, R.id.text_view_item_shoulder),
            Triple(WarcraftInfoConstant.SLOT_BACK, R.id.image_view_item_back, R.id.text_view_item_back),
            Triple(WarcraftInfoConstant.SLOT_CHEST, R.id.image_view_item_chest, R.id.text_view_item_chest),
            Triple(WarcraftInfoConstant.SLOT_SHIRT, R.id.image_view_item_shirt, R.id.text_view_item_shirt),
            Triple(WarcraftInfoConstant.SLOT_TABARD, R.id.image_view_item_tabard, R.id.text_view_item_tabard),
            Triple(WarcraftInfoConstant.SLOT_WRIST, R.id.image_view_item_wrist, R.id.text_view_item_wrist),
            Triple(WarcraftInfoConstant.SLOT_HANDS, R.id.image_view_item_hands, R.id.text_view_item_hands),
            Triple(WarcraftInfoConstant.SLOT_WAIST, R.id.image_view_item_waist, R.id.text_view_item_waist),
            Triple(WarcraftInfoConstant.SLOT_LEGS, R.id.image_view_item_legs, R.id.text_view_item_legs),
            Triple(WarcraftInfoConstant.SLOT_FEET, R.id.image_view_item_feet, R.id.text_view_item_feet),
            Triple(WarcraftInfoConstant.SLOT_FINGER_1, R.id.image_view_item_finger_1, R.id.text_view_item_finger_1),
            Triple(WarcraftInfoConstant.SLOT_FINGER_2, R.id.image_view_item_finger_2, R.id.text_view_item_finger_2),
            Triple(WarcraftInfoConstant.SLOT_TRINKET_1, R.id.image_view_item_trinket_1, R.id.text_view_item_trinket_1),
            Triple(WarcraftInfoConstant.SLOT_TRINKET_2, R.id.image_view_item_trinket_2, R.id.text_view_item_trinket_2),
            Triple(WarcraftInfoConstant.SLOT_MAIN_HAND, R.id.image_view_item_main_hand, R.id.text_view_item_main_hand),
            Triple(WarcraftInfoConstant.SLOT_OFF_HAND, R.id.image_view_item_off_hand, R.id.text_view_item_off_hand)
        )
        for (slotAndViewTriple in slotAndViewTripleList) {
            viewModel.characterItemMap[slotAndViewTriple.first]?.observe(viewLifecycleOwner, Observer { item ->
                val imageView: ImageView? = view?.findViewById(slotAndViewTriple.second)
                val textView: TextView? = view?.findViewById(slotAndViewTriple.third)
                if (item != null && imageView != null) {
                    Picasso
                        .get()
                        .load(item.mediaLink)
                        .resize(60, 60)
                        .into(imageView)
                    if (textView != null) {
                        textView.text = item.name
                    }
                }
            })
        }
    }

}