package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.Auction
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.AuctionDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.repository.ItemRepository
import javax.inject.Inject

class AuctionHouseRepository @Inject constructor(
    private val auctionDao: AuctionDao,
    private val itemRepository: ItemRepository,
    @ApplicationContext private val appContext: Context
) {
    fun getAuctionHouseDataValidity(): Boolean {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(WarcraftInfoConstant.AUCTION_HOUSE_DATA_VALIDITY_KEY, false)
    }

    fun setAuctionHouseDataValidity(value: Boolean) {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )
        sharedPreferences
            .edit()
            .putBoolean(WarcraftInfoConstant.AUCTION_HOUSE_DATA_VALIDITY_KEY, value)
            .apply()
    }

    fun getAuctionHouseListingPaged(): LiveData<PagedList<Auction>> {
        return auctionDao.getAllPaged().map {
            val itemDetailResponse = itemRepository.getItemById(it.itemId)
            val itemDetail = itemDetailResponse.data
            if (itemDetailResponse.code == WarcraftInfoConstant.API_RESPONSE_SUCCESS && itemDetail != null) {
                it.itemDetail = itemDetail
            }
            return@map it
        }.toLiveData(25)
    }
}