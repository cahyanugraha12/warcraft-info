package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.ui.character.viewmodel.model

data class CharacterItem(
    val id: Int,
    val mediaLink: String,
    val slot: String,
    val quality: String,
    val itemClass: String,
    val itemSubclass: String,
    val itemLevel: String,
    val itemName: String
)