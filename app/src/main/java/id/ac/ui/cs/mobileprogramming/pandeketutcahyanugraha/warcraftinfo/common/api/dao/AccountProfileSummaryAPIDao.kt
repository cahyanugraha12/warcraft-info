package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class AccountProfileSummary(
    val id: Int,
    @SerializedName("wow_accounts") val wowAccounts: List<WoWAccount>
)