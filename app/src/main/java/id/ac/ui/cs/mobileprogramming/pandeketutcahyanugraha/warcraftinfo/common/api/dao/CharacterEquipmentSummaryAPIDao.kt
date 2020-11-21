package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class CharacterEquipmentSummaryAPIDao(
    @SerializedName("equipped_items") val equippedItems: List<CharacterEquipmentDetailAPIDao>
)