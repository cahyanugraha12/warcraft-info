package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.Item
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.ItemDao

@Database(entities = arrayOf(Item::class), version = 1)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    // TODO Implement, remember to add to entities above too
//    abstract fun itemClassDao(): ItemClassDao
}