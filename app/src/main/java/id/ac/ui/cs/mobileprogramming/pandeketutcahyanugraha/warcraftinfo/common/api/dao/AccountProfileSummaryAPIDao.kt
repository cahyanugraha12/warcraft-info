package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class AccountProfileSummaryAPIDao(
    val id: Int,
    @SerializedName("wow_accounts") val wowAccounts: List<WoWAccountAPIDao>
)