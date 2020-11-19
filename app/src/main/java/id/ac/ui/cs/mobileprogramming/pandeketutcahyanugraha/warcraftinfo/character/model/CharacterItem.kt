package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.Item

data class CharacterItem(
    val id: Int,
    val name: String,
    val quality: String,
    val level: Int,
    val requiredLevel: Int,
    val slot: String,
    val itemClassName: String,
    val purchasePrice: Int,
    val sellPrice: Int,
    val mediaLink: String
) {
    companion object {
        fun fromItemAndSlotType(item: Item, slotType: String): CharacterItem {
            return CharacterItem(
                item.id,
                item.name,
                item.quality,
                item.level,
                item.requiredLevel,
                slotType,
                item.itemClassName,
                item.purchasePrice,
                item.sellPrice,
                item.mediaLink
            )
        }
    }
}