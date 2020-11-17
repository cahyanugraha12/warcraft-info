package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.ui.character.viewmodel.model

data class CharacterDetail(
    val id: Int,
    val name: String,
    val realm: String,
    val characterClass: String,
    val race: String,
    val gender: String,
    val faction: String,
    val level: Int
)