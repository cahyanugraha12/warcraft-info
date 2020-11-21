package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AuctionAPIDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.Item

@Entity(indices = [Index(value = ["itemId"])])
data class Auction (
    @PrimaryKey val id: Int,
    val itemId: Int,
    @Ignore var itemDetail: Item?,
    val quantity: Int,
    val unitPrice: Long?,
    val bid: Long?,
    val buyout: Long?,
    val timeLeft: String
) {
    constructor(id: Int, itemId: Int, quantity: Int, unitPrice: Long?, bid: Long?, buyout: Long?, timeLeft: String)
            : this(id, itemId, null, quantity, unitPrice, bid, buyout, timeLeft)
    companion object {
        fun fromAuctionAPIDao(auctionAPIDao: AuctionAPIDao): Auction {
            return Auction(
                auctionAPIDao.id,
                auctionAPIDao.item.id,
                null,
                auctionAPIDao.quantity,
                auctionAPIDao.unitPrice,
                auctionAPIDao.bid,
                auctionAPIDao.buyout,
                auctionAPIDao.timeLeft
            )
        }
    }
}