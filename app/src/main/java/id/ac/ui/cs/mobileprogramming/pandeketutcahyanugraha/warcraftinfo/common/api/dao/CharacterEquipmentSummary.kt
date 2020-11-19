package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class CharacterEquipmentSummary(
    @SerializedName("equipped_items") val equippedItems: List<CharacterEquipmentDetail>
)