package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class AuctionAPIDao(
    val id: Int,
    val item: GenericID,
    val quantity: Int,
    @SerializedName("unit_price") val unitPrice: Long?,
    val bid: Long?,
    val buyout: Long?,
    @SerializedName("time_left") val timeLeft: String
)