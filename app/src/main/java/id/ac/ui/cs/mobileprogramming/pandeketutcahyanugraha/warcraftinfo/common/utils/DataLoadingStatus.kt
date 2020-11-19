package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.utils

import androidx.lifecycle.MutableLiveData

data class DataLoadingStatus (
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true),
    var isSuccess: Boolean = false,
    var errorCode: Int? = null
)