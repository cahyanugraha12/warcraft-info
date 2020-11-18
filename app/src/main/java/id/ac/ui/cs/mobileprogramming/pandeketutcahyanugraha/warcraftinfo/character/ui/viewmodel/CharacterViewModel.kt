package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.repository.CharacterRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import kotlinx.coroutines.launch

class CharacterViewModel @ViewModelInject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    // TODO Maybe move to a common class
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var isSuccess: Boolean = false
    var errorCode: Int? = null

    var currentCharacterSummarySelectedPosition: MutableLiveData<Int?> = MutableLiveData(null)
    var characterSummaryList: MutableLiveData<List<CharacterSummary>> = MutableLiveData(
        listOf()
    )
//    lateinit var characterSummaryIdList: List<Int>
//    lateinit var characterSummaryMap: Map<Int, CharacterSummary>

    init {
        viewModelScope.launch {
            val characterSummaryListAPIResponse = characterRepository.getCharacterSummary()
            if (characterSummaryListAPIResponse is APIResponse.Success) {
                isSuccess = true
                if (characterSummaryListAPIResponse.data != null && characterSummaryListAPIResponse.data.isNotEmpty()) {
                    currentCharacterSummarySelectedPosition.value = 1
                    characterSummaryList.value = characterSummaryListAPIResponse.data
                }
            } else {
                errorCode = characterSummaryListAPIResponse.code
            }
            isLoading.value = false
        }
    }
}