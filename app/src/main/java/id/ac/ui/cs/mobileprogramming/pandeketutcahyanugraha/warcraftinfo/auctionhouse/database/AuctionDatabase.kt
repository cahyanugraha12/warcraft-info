package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.Auction
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.AuctionDao

@Database(entities = arrayOf(Auction::class), version = 1)
abstract class AuctionDatabase : RoomDatabase() {
    abstract fun auctionDao(): AuctionDao
}