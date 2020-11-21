package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

import com.google.gson.annotations.SerializedName

data class CharacterAPIDao(
    val id: Int,
    val name: String,
    val realm: CharacterRealmAPIDao,
    @SerializedName("playable_class") val playableClass: GenericIDName,
    @SerializedName("playable_race") val playableRace: GenericIDName,
    val gender: GenericTypeName,
    val faction: GenericTypeName,
    val level: Int
)