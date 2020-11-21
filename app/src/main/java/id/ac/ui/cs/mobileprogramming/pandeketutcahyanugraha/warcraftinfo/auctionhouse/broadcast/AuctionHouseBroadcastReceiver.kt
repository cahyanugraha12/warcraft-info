package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.broadcast

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.service.AuctionHouseJobService
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant

class AuctionHouseBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            var jobAlreadyScheduled = false
            jobScheduler.allPendingJobs.forEach { job ->
                if (job.id == WarcraftInfoConstant.AUCTION_HOUSE_JOB_SERVICE_JOB_ID) {
                    jobAlreadyScheduled = true
                }
            }

            if (!jobAlreadyScheduled) {
                jobScheduler.schedule(AuctionHouseJobService.auctionHouseJobBuilder(context))
            }
        }
    }
}