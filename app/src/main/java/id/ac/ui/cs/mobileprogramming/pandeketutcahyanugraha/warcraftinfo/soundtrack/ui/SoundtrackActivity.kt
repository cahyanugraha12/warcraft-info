package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.SoundtrackMetadataDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.databinding.SoundtrackActivityBinding
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.repository.SoundtrackRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.service.SoundtrackPlayerService
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.ui.viewmodel.SoundtrackViewModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import javax.inject.Inject

@AndroidEntryPoint
class SoundtrackActivity : AppCompatActivity() {

    private lateinit var binding: SoundtrackActivityBinding
    @Inject
    lateinit var soundtrackRepository: SoundtrackRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SoundtrackActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            val viewModel: SoundtrackViewModel by viewModels()
            viewModel.soundtrackDataLoadingStatus.isLoading.observe(this, Observer { isLoading ->
                if (isLoading) {
                    binding.containerLoadingSoundtrack.visibility = View.VISIBLE
                    binding.containerDataSoundtrack.visibility = View.GONE
                } else {
                    binding.containerLoadingSoundtrack.visibility = View.GONE
                    binding.containerDataSoundtrack.visibility = View.VISIBLE
                }
            })
            viewModel.soundtrackList.observe(this, Observer { soundtrackList ->
                if (soundtrackList.isEmpty()) {
                    viewModel.soundtrackDataLoadingStatus.isSuccess = false
                } else {
                    binding.recyclerViewSoundtrackMetadata.layoutManager = LinearLayoutManager(this)
                    binding.recyclerViewSoundtrackMetadata.adapter = SoundtrackListAdapter(this, soundtrackList)
                }
            })
            binding.imageButtonPrevious.setOnClickListener {
                sendActionToSoundtrackPlayerService(WarcraftInfoConstant.BROADCAST_PREVIOUS_SOUNDTRACK)
            }
            binding.imageButtonPause.setOnClickListener {
                sendActionToSoundtrackPlayerService(WarcraftInfoConstant.BROADCAST_PAUSE_SOUNDTRACK)
            }
            binding.imageButtonResume.setOnClickListener {
                sendActionToSoundtrackPlayerService(WarcraftInfoConstant.BROADCAST_RESUME_SOUNDTRACK)
            }
            binding.imageButtonNext.setOnClickListener {
                sendActionToSoundtrackPlayerService(WarcraftInfoConstant.BROADCAST_NEXT_SOUNDTRACK)
            }
        } else {
            binding.containerLoadingSoundtrack.visibility = View.VISIBLE
            binding.containerDataSoundtrack.visibility = View.GONE
            binding.messageLoadingSoundtrack.text = getString(R.string.message_network_is_not_connected)
        }
    }

    // Adapter for RecycleView
    inner class SoundtrackListAdapter(
        val context: Context,
        private val soundtrackList: List<SoundtrackMetadataDao>
    ) : RecyclerView.Adapter<SoundtrackListAdapter.SoundtrackListViewHolder>() {
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val position = v.tag as Int
                playAudio(position)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundtrackListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.soundtrack_metadata_item, parent, false)
            return SoundtrackListViewHolder(view)
        }

        override fun onBindViewHolder(holder: SoundtrackListViewHolder, position: Int) {
            val soundtrack = soundtrackList[position]
            if (soundtrack.cover != "") {
                Picasso
                    .get()
                    .load(WarcraftInfoConstant.TARANZHI_API_URI + soundtrack.cover)
                    .resize(60, 60)
                    .into(holder.albumCover)
            }
            holder.title.text = soundtrack.title
            holder.artists.text = soundtrack.artists
            holder.album.text = soundtrack.album

            with(holder.itemView) {
                tag = position
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = soundtrackList.size

        inner class SoundtrackListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val albumCover: ImageView = view.findViewById(R.id.image_view_album_cover)
            val title: TextView = view.findViewById(R.id.text_view_soundtrack_metadata_item_detail_title)
            val artists: TextView = view.findViewById(R.id.text_view_soundtrack_metadata_item_detail_artists)
            val album: TextView = view.findViewById(R.id.text_view_soundtrack_metadata_item_detail_album)
        }
    }

    private var soundtrackPlayerService: SoundtrackPlayerService? = null
    private var serviceBound = false

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: SoundtrackPlayerService.SoundtrackPlayerServiceBinder = service as SoundtrackPlayerService.SoundtrackPlayerServiceBinder
            soundtrackPlayerService = binder.getService()
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("SoundtrackPlayerServiceState", serviceBound)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceBound = savedInstanceState.getBoolean("SoundtrackPlayerServiceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            soundtrackPlayerService?.stopSelf()
        }
    }

    private fun playAudio(index: Int) {
        soundtrackRepository.setSoundtrackIndex(index)
        if (!serviceBound) {
            val soundtrackPlayerServiceIntent = Intent(this, SoundtrackPlayerService::class.java)
            startService(soundtrackPlayerServiceIntent)
            bindService(soundtrackPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            val broadcastIntent = Intent(WarcraftInfoConstant.BROADCAST_PLAY_NEW_SOUNDTRACK)
            sendBroadcast(broadcastIntent)
        }
    }

    private fun sendActionToSoundtrackPlayerService(action: String) {
        if (serviceBound) {
            val broadcastIntent = Intent(action)
            sendBroadcast(broadcastIntent)
        }
    }
}