package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterItem
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.repository.CharacterRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.utils.DataLoadingStatus
import kotlinx.coroutines.launch

class CharacterViewModel @ViewModelInject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    val characterListLoadingStatus: DataLoadingStatus = DataLoadingStatus()
    val characterDetailLoadingStatus: DataLoadingStatus = DataLoadingStatus()

    var currentCharacterSummarySelectedPosition: MutableLiveData<Int?> = MutableLiveData(null)
    var characterSummaryList: MutableLiveData<List<CharacterSummary>> = MutableLiveData(
        listOf()
    )
    @Suppress("UNCHECKED_CAST")
    var characterItemMap: MutableMap<String, MutableLiveData<CharacterItem?>> = mutableMapOf(
        WarcraftInfoConstant.SLOT_HEAD to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_NECK to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_SHOULDER to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_BACK to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_CHEST to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_SHIRT to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_TABARD to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_WRIST to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_HANDS to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_WAIST to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_LEGS to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_FEET to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_FINGER_1 to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_FINGER_2 to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_TRINKET_1 to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_TRINKET_2 to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_MAIN_HAND to MutableLiveData(null) as MutableLiveData<CharacterItem?>,
        WarcraftInfoConstant.SLOT_OFF_HAND to MutableLiveData(null) as MutableLiveData<CharacterItem?>
    )

    init {
        getCharacterSummary()
    }

    fun getCharacterSummary() {
        viewModelScope.launch {
            characterListLoadingStatus.isLoading.value = true
            val characterSummaryListAPIResponse = characterRepository.getCharacterSummary()
            if (characterSummaryListAPIResponse is APIResponse.Success) {
                characterListLoadingStatus.isSuccess = true
                if (characterSummaryListAPIResponse.data != null && characterSummaryListAPIResponse.data.isNotEmpty()) {
                    characterSummaryList.value = characterSummaryListAPIResponse.data.sortedBy {
                        it.name
                    }
                    currentCharacterSummarySelectedPosition.value = 1
                }
            } else {
                characterListLoadingStatus.errorCode = characterSummaryListAPIResponse.code
            }
            characterListLoadingStatus.isLoading.value = false
        }
    }

    fun getCharacterItemList() {
        viewModelScope.launch {
            characterDetailLoadingStatus.isLoading.value = true
            val currentSelectedCharacterSummary = characterSummaryList.value?.get(
                currentCharacterSummarySelectedPosition.value!!
            )
            val characterListItemAPIResponse = characterRepository.getCharacterItemList(
                currentSelectedCharacterSummary?.realmSlug!!,
                currentSelectedCharacterSummary.name
            )
            if (characterListItemAPIResponse is APIResponse.Success) {
                characterDetailLoadingStatus.isSuccess = true
                if (characterListItemAPIResponse.data != null && characterListItemAPIResponse.data.isNotEmpty()) {
                    for (characterItem in characterListItemAPIResponse.data) {
                        characterItemMap[characterItem.slot]?.value = characterItem
                    }
                }
            } else {
                characterDetailLoadingStatus.errorCode = characterListItemAPIResponse.code
            }
            characterDetailLoadingStatus.isLoading.value = false
        }
    }
}