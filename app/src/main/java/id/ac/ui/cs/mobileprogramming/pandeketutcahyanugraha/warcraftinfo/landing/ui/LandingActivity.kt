package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.landing.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
// Used for Hilt Dependency Injection, move to wherever activity assigned as launcher
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.WarcraftInfoApplication
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.ui.AuctionHouseActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.CharacterActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.LandingActivityBinding

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    private lateinit var binding: LandingActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LandingActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.imageButtonProfile.setOnClickListener {
            val intent = Intent(this, CharacterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        binding.linearLayoutButtonProfile.setOnClickListener {
            val intent = Intent(this, CharacterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        binding.imageButtonAuctionHouse.setOnClickListener {
            val intent = Intent(this, AuctionHouseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        binding.linearLayoutButtonAuctionHouse.setOnClickListener {
            val intent = Intent(this, AuctionHouseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
    }
}