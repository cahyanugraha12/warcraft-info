package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.ItemMediaAPIDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.ItemAPIDao as ItemDao

@Entity
data class Item (
    @PrimaryKey val id: Int,
    val name: String,
    val quality: String,
    val level: Int,
    val requiredLevel: Int,
    val itemClassId: Int,
    val itemClassName: String,
    val purchasePrice: Int,
    val sellPrice: Int,
    val mediaLink: String
) {
    companion object {
        fun fromItemAndItemMedia(item: ItemDao, itemMedia: ItemMediaAPIDao): Item {
            var mediaLink = ""
            for (media in itemMedia.assets) {
                if (media.key == WarcraftInfoConstant.MEDIA_ICON_KEY) {
                    mediaLink = media.value
                }
            }

            return Item(
                item.id,
                item.name,
                item.quality.name,
                item.level,
                item.requiredLevel,
                item.itemClass.id,
                item.itemClass.name,
                item.purchasePrice,
                item.sellPrice,
                mediaLink
            )
        }
    }
}