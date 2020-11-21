package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class ItemAPIDao (
    val id: Int,
    val name: String,
    val quality: GenericTypeName,
    val level: Int,
    @SerializedName("required_level") val requiredLevel: Int,
    @SerializedName("item_class") val itemClass: GenericIDName,
    @SerializedName("purchase_price") val purchasePrice: Int,
    @SerializedName("sell_price") val sellPrice: Int
)