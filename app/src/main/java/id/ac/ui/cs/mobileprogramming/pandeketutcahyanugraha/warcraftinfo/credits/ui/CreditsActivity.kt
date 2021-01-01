package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.credits.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.CreditsActivityBinding


class CreditsActivity : AppCompatActivity() {
    private lateinit var gLView: MyGLSurfaceView
    private lateinit var binding: CreditsActivityBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreditsActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        gLView = findViewById(R.id.view_openGL)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                Array<String>(2) {
                    Manifest.permission.ACCESS_FINE_LOCATION
                    Manifest.permission.ACCESS_COARSE_LOCATION
                },
                WarcraftInfoConstant.ACCESS_LOCATION_REQUEST_CODE)
        } else {
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            WarcraftInfoConstant.ACCESS_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    binding.containerDataCreditsActivity.visibility = View.GONE
                    binding.containerErrorCreditsActivity.visibility = View.VISIBLE
                    binding.messageErrorCreditsActivity.text = getString(R.string.error_credits_activity_require_location)
                }
                return
            }
        }
    }

    private fun getLocation() {
        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.containerDataCreditsActivity.visibility = View.GONE
            binding.containerErrorCreditsActivity.visibility = View.VISIBLE
            binding.messageErrorCreditsActivity.text = getString(R.string.error_credits_activity_require_location)
        }
        binding.containerDataCreditsActivity.visibility = View.VISIBLE
        binding.containerErrorCreditsActivity.visibility = View.GONE
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                binding.containerDataCreditsActivity.visibility = View.VISIBLE
                binding.containerErrorCreditsActivity.visibility = View.GONE
                binding.textViewCredits.text = getString(
                    R.string.credits,
                    locationResult.lastLocation.latitude.toString(),
                    locationResult.lastLocation.longitude.toString()
                )
            }
        }
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    binding.textViewCredits.text = getString(
                        R.string.credits,
                        location.latitude.toString(),
                        location.longitude.toString()
                    )
                } else {
                    binding.textViewCredits.text = getString(R.string.error_credits_activity_cannot_access_location)
                }
            }
        }
}