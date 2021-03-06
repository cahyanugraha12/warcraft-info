package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model

import androidx.room.Entity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.ItemClassesIndexAPIDao

@Entity
data class ItemClass (
    val id: Int,
    val name: String
) {
    companion object {
        fun fromItemClassesIndex(itemClassesIndex: ItemClassesIndexAPIDao): List<ItemClass> {
            val result: MutableList<ItemClass> = mutableListOf()
            for (itemClass in itemClassesIndex.itemClassList) {
                result.add(ItemClass(
                    itemClass.id,
                    itemClass.name
                ))
            }
            return result
        }
    }
}