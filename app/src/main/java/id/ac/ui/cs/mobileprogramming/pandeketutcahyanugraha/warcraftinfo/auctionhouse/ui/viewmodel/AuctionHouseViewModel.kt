package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.Auction
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.repository.AuctionHouseRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterItem
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.repository.CharacterRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.utils.DataLoadingStatus
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.Item
import kotlinx.coroutines.launch

class AuctionHouseViewModel @ViewModelInject constructor(
    private val auctionHouseRepository: AuctionHouseRepository
) : ViewModel() {
    val auctionHouseListingDataLoadingStatus: DataLoadingStatus = DataLoadingStatus()
    lateinit var auctionHouseListingData: LiveData<PagedList<Auction>>

    init {
        auctionHouseListingData = auctionHouseRepository.getAuctionHouseListingPaged()
        auctionHouseListingDataLoadingStatus.isLoading.value = false
    }
}