package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<Item>

    @Query("SELECT * FROM item WHERE id = :id")
    fun getById(id: Int): Item?

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg items: Item)

    @Delete
    fun delete(item: Item)
}