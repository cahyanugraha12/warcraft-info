package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.ui

import android.app.job.JobScheduler
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.Auction
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.repository.AuctionHouseRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.service.AuctionHouseJobService
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.ui.viewmodel.AuctionHouseViewModel
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.AuctionHouseActivityBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.repository.ItemRepository
import javax.inject.Inject

@AndroidEntryPoint
class AuctionHouseActivity : AppCompatActivity() {

    private lateinit var binding: AuctionHouseActivityBinding
    @Inject
    lateinit var auctionHouseRepository: AuctionHouseRepository
    @Inject
    lateinit var itemRepository: ItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule Job
        val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        var jobAlreadyScheduled = false
        jobScheduler.allPendingJobs.forEach { job ->
            if (job.id == WarcraftInfoConstant.AUCTION_HOUSE_JOB_SERVICE_JOB_ID) {
                jobAlreadyScheduled = true
            }
        }

        if (!jobAlreadyScheduled) {
            jobScheduler.schedule(AuctionHouseJobService.auctionHouseJobBuilder(applicationContext))
        }

        binding = AuctionHouseActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val auctionHouseDataValidity = auctionHouseRepository.getAuctionHouseDataValidity()
        if (auctionHouseDataValidity && jobAlreadyScheduled) {
            val viewModel: AuctionHouseViewModel by viewModels()
            val adapter = AuctionHouseItemListingAdapter(this, itemRepository)
            binding.recyclerViewAuctionHouseListing.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewAuctionHouseListing.adapter = adapter

            viewModel.auctionHouseListingDataLoadingStatus.isLoading.observe(this, Observer { isLoading ->
                if (isLoading) {
                    binding.containerLoadingAuctionHouseListing.visibility = View.VISIBLE
                    binding.containerDataAuctionHouseListing.visibility = View.GONE
                } else {
                    binding.containerLoadingAuctionHouseListing.visibility = View.GONE
                    binding.containerDataAuctionHouseListing.visibility = View.VISIBLE
                }
            })
            viewModel.auctionHouseListingData.observe(this, Observer {
                adapter.submitList(it)
            })
        } else {
            binding.containerLoadingAuctionHouseListing.visibility = View.VISIBLE
            binding.containerDataAuctionHouseListing.visibility = View.GONE
            binding.messageLoadingAuctionHouseListing.text = getString(
                R.string.message_auction_house_listing_data_invalid
            )
        }
    }

    class AuctionHouseItemListingAdapter(
        val context: Context,
        val itemRepository: ItemRepository
    ) : PagedListAdapter<Auction, AuctionHouseItemListingAdapter.AuctionHouseItemListingViewHolder>(DIFF_CALLBACK) {
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val position = v.tag as Int
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionHouseItemListingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.auction_house_listing_item, parent, false)
            return AuctionHouseItemListingViewHolder(view)
        }

        override fun onBindViewHolder(holder: AuctionHouseItemListingViewHolder, position: Int) {
            val auctionItemListing: Auction? = getItem(position)
            if (auctionItemListing != null) {
                if (auctionItemListing.bid != null) {
                    val gold = auctionItemListing.bid / 10000
                    val silver = (auctionItemListing.bid % 10000) / 100
                    val copper = (auctionItemListing.bid % 100)
                    holder.bidPriceView.text = context.getString(
                        R.string.template_bid_price,
                        gold,
                        silver,
                        copper
                    )
                }
                if (auctionItemListing.buyout != null) {
                    val gold = auctionItemListing.buyout / 10000
                    val silver = (auctionItemListing.buyout % 10000) / 100
                    val copper = (auctionItemListing.buyout % 100)
                    holder.buyoutPriceView.text = context.getString(
                        R.string.template_buyout_price,
                        gold,
                        silver,
                        copper
                    )
                }
                if (auctionItemListing.unitPrice != null) {
                    val gold = auctionItemListing.unitPrice / 10000
                    val silver = (auctionItemListing.unitPrice % 10000) / 100
                    val copper = (auctionItemListing.unitPrice % 100)
                    holder.unitPricePriceView.text = context.getString(
                        R.string.template_unit_price,
                        gold,
                        silver,
                        copper
                    )
                }
                when (auctionItemListing.timeLeft) {
                    "SHORT" -> holder.timeLeftView.text = context.getString(R.string.text_auction_time_left_short)
                    "MEDIUM" -> holder.timeLeftView.text = context.getString(R.string.text_auction_time_left_medium)
                    "LONG" -> holder.timeLeftView.text = context.getString(R.string.text_auction_time_left_long)
                    "VERY_LONG" -> holder.timeLeftView.text = context.getString(R.string.text_auction_time_left_very_long)
                    else -> holder.timeLeftView.text = context.getString(R.string.fallback_unit_price_null)
                }

                holder.itemNameView.text = auctionItemListing.itemDetail?.name
                holder.itemQualityView.text = auctionItemListing.itemDetail?.quality
                Picasso.get().load(auctionItemListing.itemDetail?.mediaLink).into(holder.itemIconView)
            }
        }

        companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Auction>() {
                override fun areItemsTheSame(oldItem: Auction, newItem: Auction) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Auction, newItem: Auction): Boolean {
                    return oldItem == newItem
                }
            }
        }

        inner class AuctionHouseItemListingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val bidPriceView: TextView = view.findViewById(R.id.text_view_bid_price)
            val buyoutPriceView: TextView = view.findViewById(R.id.text_view_buyout_price)
            val unitPricePriceView: TextView = view.findViewById(R.id.text_view_unit_price)
            val timeLeftView: TextView = view.findViewById(R.id.text_view_time_left)

            val itemIconView: ImageView = view.findViewById(R.id.image_view_item_icon)
            val itemNameView: TextView = view.findViewById(R.id.text_view_auction_house_listing_item_detail_name)
            val itemQualityView: TextView = view.findViewById(R.id.text_view_auction_house_listing_item_detail_quality)
        }
    }
}