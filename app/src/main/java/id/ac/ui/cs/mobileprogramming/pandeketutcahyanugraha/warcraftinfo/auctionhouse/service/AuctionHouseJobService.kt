package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.Auction
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.AuctionDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.repository.AuctionHouseRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.ui.AuctionHouseActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository.AuthRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.BlizzardAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AuctionHouseJobService: JobService() {

    @Inject
    lateinit var blizzardAPI: BlizzardAPI
    @Inject
    lateinit var authRepository: AuthRepository
    @Inject
    lateinit var auctionHouseRepository: AuctionHouseRepository
    @Inject
    lateinit var auctionDao: AuctionDao

    override fun onStartJob(params: JobParameters?): Boolean {
        GlobalScope.launch (Dispatchers.IO) {
            // Invalidate and delete previous auction house data
            auctionHouseRepository.setAuctionHouseDataValidity(false)

            auctionDao.deleteAll()

            // Create Notification Channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = getString(R.string.name_notification_channel_auction_house_job)
                val descriptionText = getString(R.string.description_notification_channel_auction_house_job)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    WarcraftInfoConstant.AUCTION_HOUSE_JOB_NOTIFICATION_CHANNEL_ID,
                    name,
                    importance
                ).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(
                this@AuctionHouseJobService,
                WarcraftInfoConstant.AUCTION_HOUSE_JOB_NOTIFICATION_CHANNEL_ID
            ).apply {
                setContentTitle(getString(R.string.content_title_notification_auction_house_job))
                setContentText(getString(R.string.content_text_in_progress_notification_auction_house_job))
                setSmallIcon(R.drawable.auction_house_icon)
                priority = NotificationCompat.PRIORITY_LOW
            }
            builder.setProgress(0, 0, true)
            NotificationManagerCompat
                .from(this@AuctionHouseJobService)
                .notify(WarcraftInfoConstant.AUCTION_HOUSE_JOB_NOTIFICATION_ID, builder.build())

            val staticToken = authRepository.getStaticToken(true)!!
            val getAuctionHouseDataResponse = withContext(Dispatchers.IO) {
                blizzardAPI.getAuctionHouseData(
                    WarcraftInfoConstant.NAMESPACE_DYNAMIC,
                    WarcraftInfoConstant.LOCALE,
                    staticToken
                ).execute()
            }
            val getAuctionHouseDataResponseBody = getAuctionHouseDataResponse.body()

            if (!getAuctionHouseDataResponse.isSuccessful || getAuctionHouseDataResponseBody == null) {
                builder
                    .setContentText(getString(R.string.content_text_failed_notification_auction_house_job))
                    .setProgress(0, 0, false)
                NotificationManagerCompat
                    .from(this@AuctionHouseJobService)
                    .notify(WarcraftInfoConstant.AUCTION_HOUSE_JOB_NOTIFICATION_ID, builder.build())
            } else {
                getAuctionHouseDataResponseBody.auctions.map { auctionAPIDao ->
                    GlobalScope.async(Dispatchers.IO) {
                        val auctionModel = Auction.fromAuctionAPIDao(auctionAPIDao)
                        auctionDao.insertAll(auctionModel)
                    }
                }.awaitAll()

                // Set fetching success
                val resultIntent = Intent(applicationContext, AuctionHouseActivity::class.java)
                val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
                    addNextIntentWithParentStack(resultIntent)
                    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                }

                builder.setContentText(getString(R.string.content_text_completed_notification_auction_house_job))
                    .setContentIntent(resultPendingIntent)
                    .setProgress(0, 0, false)
                NotificationManagerCompat
                    .from(this@AuctionHouseJobService)
                    .notify(WarcraftInfoConstant.AUCTION_HOUSE_JOB_NOTIFICATION_ID, builder.build())

                auctionHouseRepository.setAuctionHouseDataValidity(true)
            }
        }

        return true;
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        // Set fetching failed
        val builder = NotificationCompat.Builder(
            this@AuctionHouseJobService,
            WarcraftInfoConstant.AUCTION_HOUSE_JOB_NOTIFICATION_CHANNEL_ID
        ).apply {
            setContentTitle(getString(R.string.content_title_notification_auction_house_job))
            setContentText(getString(R.string.content_text_failed_notification_auction_house_job))
            setSmallIcon(R.drawable.auction_house_icon)
            setProgress(0, 0, false)
            priority = NotificationCompat.PRIORITY_LOW
        }
        NotificationManagerCompat
            .from(this@AuctionHouseJobService)
            .notify(WarcraftInfoConstant.AUCTION_HOUSE_JOB_NOTIFICATION_ID, builder.build())
        return false;
    }

    companion object {
        fun auctionHouseJobBuilder(context: Context): JobInfo {
            return JobInfo.Builder(
                WarcraftInfoConstant.AUCTION_HOUSE_JOB_SERVICE_JOB_ID,
                ComponentName(context, AuctionHouseJobService::class.java)
            )
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(3 * 60 * 60 * 1000)
                .build()
        }
    }
}
