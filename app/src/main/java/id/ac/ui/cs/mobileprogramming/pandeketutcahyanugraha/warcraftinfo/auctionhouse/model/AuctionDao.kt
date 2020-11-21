package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface AuctionDao {
    @Query("SELECT * FROM auction ORDER BY itemId")
    fun getAll(): List<Auction>

    @Query("SELECT * FROM auction ORDER BY itemId")
    fun getAllPaged(): DataSource.Factory<Int, Auction>

    @Query("SELECT * FROM auction WHERE id = :id")
    fun getById(id: Int): Auction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg auctions: Auction)

    @Delete
    fun delete(auction: Auction)

    @Query("DELETE FROM auction")
    fun deleteAll()
}