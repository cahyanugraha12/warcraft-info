package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.ui.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.LandingActivityBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: LandingActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LandingActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}