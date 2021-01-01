package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.landing.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
// Used for Hilt Dependency Injection, move to wherever activity assigned as launcher
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.WarcraftInfoApplication
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.ui.AuctionHouseActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.calculator.ui.CalculatorActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.CharacterActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.credits.ui.CreditsActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.LandingActivityBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.ui.SoundtrackActivity

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
        binding.imageButtonSoundtrack.setOnClickListener {
            val intent = Intent(this, SoundtrackActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        binding.linearLayoutButtonSoundtrack.setOnClickListener {
            val intent = Intent(this, SoundtrackActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        binding.buttonCredits.setOnClickListener {
            val intent = Intent(this, CreditsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        binding.buttonCalculator.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
    }
}