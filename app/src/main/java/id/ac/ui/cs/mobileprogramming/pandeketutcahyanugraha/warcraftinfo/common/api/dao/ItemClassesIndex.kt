package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class ItemClassesIndex(
    @SerializedName("item_classes") val itemClassList: List<GenericIDName>
)