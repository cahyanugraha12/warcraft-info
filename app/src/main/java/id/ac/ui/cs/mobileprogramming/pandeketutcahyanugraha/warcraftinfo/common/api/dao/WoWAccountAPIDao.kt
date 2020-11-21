package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao

data class WoWAccountAPIDao(
    val id: Int,
    val characters: List<CharacterAPIDao>
)