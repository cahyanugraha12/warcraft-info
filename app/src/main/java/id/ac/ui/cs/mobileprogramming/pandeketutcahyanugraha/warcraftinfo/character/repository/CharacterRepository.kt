package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.BlizzardAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AccountProfileSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.Character
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val blizzardAPI: BlizzardAPI,
    @ApplicationContext private val appContext: Context
) {
    suspend fun getCharacterSummary(): APIResponse<List<CharacterSummary>> {
        val sharedPreferences = appContext.getSharedPreferences(
            appContext.getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        val accessToken = sharedPreferences.getString(appContext.getString(R.string.access_token_key), null)
            ?: return APIResponse.Failed(null, WarcraftInfoConstant.ACCESS_TOKEN_INVALID)

        val profileSummaryResponseDeferred = GlobalScope.async(Dispatchers.IO) {
            blizzardAPI.getProfileSummary(WarcraftInfoConstant.NAMESPACE_PROFILE, WarcraftInfoConstant.LOCALE, accessToken).execute()
        }
        val profileSummaryResponse = profileSummaryResponseDeferred.await()

        if (!profileSummaryResponse.isSuccessful || profileSummaryResponse.body() == null) {
            return APIResponse.Failed(null, profileSummaryResponse.code())
        }

        val characterSummaryList: MutableList<CharacterSummary> = mutableListOf()
        profileSummaryResponse.body()!!.wowAccounts.map { account ->
            account.characters.map { character ->
                GlobalScope.async(Dispatchers.IO) {
                    val characterMediaSummaryResponseDeferred = GlobalScope.async(Dispatchers.IO) {
                        blizzardAPI.getCharacterMedia(
                            character.realm.slug,
                            character.name.toLowerCase(Locale.ROOT),
                            WarcraftInfoConstant.NAMESPACE_PROFILE,
                            WarcraftInfoConstant.LOCALE,
                            accessToken
                        ).execute()
                    }
                    val characterMediaSummaryResponse = characterMediaSummaryResponseDeferred.await()
                    val characterMediaSummaryResponseBody = characterMediaSummaryResponse.body()

                    if (characterMediaSummaryResponse.isSuccessful && characterMediaSummaryResponseBody != null) {
                        characterSummaryList.add(CharacterSummary.fromCharacterAndMedia(
                            character,
                            characterMediaSummaryResponseBody
                        ))
                    }
                }
            }.awaitAll()
        }
        return APIResponse.Success(characterSummaryList, WarcraftInfoConstant.API_RESPONSE_SUCCESS)
    }
}