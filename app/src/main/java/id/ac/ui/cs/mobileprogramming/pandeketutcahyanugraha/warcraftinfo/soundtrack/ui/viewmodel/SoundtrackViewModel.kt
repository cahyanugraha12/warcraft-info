package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.SoundtrackMetadataDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.utils.DataLoadingStatus
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.repository.SoundtrackRepository
import kotlinx.coroutines.launch

class SoundtrackViewModel @ViewModelInject constructor(
    private val soundtrackRepository: SoundtrackRepository
) : ViewModel() {
    val soundtrackDataLoadingStatus: DataLoadingStatus = DataLoadingStatus()
    var soundtrackList: MutableLiveData<List<SoundtrackMetadataDao>> = MutableLiveData(
        listOf()
    )

    init {
        getSoundtrackList()
    }

    private fun getSoundtrackList() {
        viewModelScope.launch {
            soundtrackDataLoadingStatus.isLoading.value = true
            val soundtrackMetadataListAPIResponse = soundtrackRepository.fetchSoundtrackMetadata()
            if (soundtrackMetadataListAPIResponse is APIResponse.Success) {
                soundtrackDataLoadingStatus.isSuccess = true
                if (soundtrackMetadataListAPIResponse.data != null && soundtrackMetadataListAPIResponse.data.isNotEmpty()) {
                    val soundtrackMetadataListData = soundtrackMetadataListAPIResponse.data
                    soundtrackRepository.setSoundtrackMetadata(soundtrackMetadataListData)
                    soundtrackList.value = soundtrackMetadataListData
                }
            } else {
                soundtrackDataLoadingStatus.errorCode = soundtrackMetadataListAPIResponse.code
            }
            soundtrackDataLoadingStatus.isLoading.value = false
        }
    }
}