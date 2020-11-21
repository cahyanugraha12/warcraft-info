package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository.AuthRepository
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterItem
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummaryDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.BlizzardAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.TaranzhiAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.repository.ItemRepository
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val blizzardAPI: BlizzardAPI,
    private val taranzhiAPI: TaranzhiAPI,
    private val characterSummaryDao: CharacterSummaryDao,
    private val authRepository: AuthRepository,
    private val itemRepository: ItemRepository,
    @ApplicationContext private val appContext: Context
) {
    suspend fun getCharacterSummary(): APIResponse<List<CharacterSummary>> {
        val useDummyData = authRepository.getUseDummyDataFlag()
        var accessToken = ""
        if (!useDummyData) {
            accessToken = authRepository.getAccessToken()
                ?: return APIResponse.Failed(null, WarcraftInfoConstant.ACCESS_TOKEN_INVALID)
        }

        val profileSummaryResponseDeferred = GlobalScope.async(Dispatchers.IO) {
            if (!useDummyData) {
                blizzardAPI.getProfileSummary(
                    WarcraftInfoConstant.NAMESPACE_PROFILE,
                    WarcraftInfoConstant.LOCALE,
                    accessToken
                ).execute()
            } else {
                taranzhiAPI.getCharacterSummary().execute()
            }
        }
        val profileSummaryResponse = profileSummaryResponseDeferred.await()

        if (!profileSummaryResponse.isSuccessful || profileSummaryResponse.body() == null) {
            return APIResponse.Failed(null, profileSummaryResponse.code())
        }

        val characterSummaryList: MutableList<CharacterSummary> = mutableListOf()
        profileSummaryResponse.body()!!.wowAccounts.map { account ->
            account.characters.map { character ->
                GlobalScope.async(Dispatchers.IO) {
                    var characterSummary = characterSummaryDao.getById(character.id)

                    if (characterSummary == null) {
                        val characterMediaSummaryResponseDeferred = GlobalScope.async(Dispatchers.IO) {
                            if (!useDummyData) {
                                blizzardAPI.getCharacterMedia(
                                    character.realm.slug,
                                    character.name.toLowerCase(Locale.ROOT),
                                    WarcraftInfoConstant.NAMESPACE_PROFILE,
                                    WarcraftInfoConstant.LOCALE,
                                    accessToken
                                ).execute()
                            } else {
                                taranzhiAPI.getCharacterMedia(
                                    character.realm.slug,
                                    character.name.toLowerCase(Locale.ROOT)
                                ).execute()
                            }
                        }
                        val characterMediaSummaryResponse = characterMediaSummaryResponseDeferred.await()
                        val characterMediaSummaryResponseBody = characterMediaSummaryResponse.body()

                        if (characterMediaSummaryResponse.isSuccessful && characterMediaSummaryResponseBody != null) {
                            characterSummary = CharacterSummary.fromCharacterAndMedia(
                                character,
                                characterMediaSummaryResponseBody
                            )
                            characterSummaryDao.insertAll(characterSummary)
                        }
                    }

                    if (characterSummary != null) {
                        characterSummaryList.add(characterSummary)
                    }
                }
            }.awaitAll()
        }
        return APIResponse.Success(characterSummaryList, WarcraftInfoConstant.API_RESPONSE_SUCCESS)
    }

    suspend fun getCharacterItemList(characterRealmSlug: String, characterName: String)
            : APIResponse<List<CharacterItem>> {
        val useDummyData = authRepository.getUseDummyDataFlag()
        var accessToken = ""
        if (!useDummyData) {
            accessToken = authRepository.getAccessToken()
                ?: return APIResponse.Failed(null, WarcraftInfoConstant.ACCESS_TOKEN_INVALID)
        }

        val characterEquipmentSummaryResponseDeferred = GlobalScope.async(Dispatchers.IO) {
            if (!useDummyData) {
                blizzardAPI.getCharacterEquipment(
                    characterRealmSlug,
                    characterName.toLowerCase(Locale.ROOT),
                    WarcraftInfoConstant.NAMESPACE_PROFILE,
                    WarcraftInfoConstant.LOCALE,
                    accessToken
                ).execute()
            } else {
                taranzhiAPI.getCharacterEquipment(
                    characterRealmSlug,
                    characterName.toLowerCase(Locale.ROOT)
                ).execute()
            }
        }
        val characterEquipmentSummaryResponse = characterEquipmentSummaryResponseDeferred.await()
        val characterEquipmentSummaryResponseBody = characterEquipmentSummaryResponse.body()

        if (!characterEquipmentSummaryResponse.isSuccessful || characterEquipmentSummaryResponseBody == null) {
            return APIResponse.Failed(null, characterEquipmentSummaryResponse.code())
        }

        val characterEquipmentList: MutableList<CharacterItem> = mutableListOf()
        characterEquipmentSummaryResponseBody.equippedItems.map { equipment ->
            GlobalScope.async(Dispatchers.IO) {
                val equipmentDetail = itemRepository.getItemById(equipment.item.id)
                if (equipmentDetail.code == WarcraftInfoConstant.API_RESPONSE_SUCCESS) {
                    characterEquipmentList.add(CharacterItem.fromItemAndSlotType(
                        equipmentDetail.data!!, equipment.slot.type
                    ))
                }
            }
        }.awaitAll()

        return APIResponse.Success(characterEquipmentList, WarcraftInfoConstant.API_RESPONSE_SUCCESS)
    }
}