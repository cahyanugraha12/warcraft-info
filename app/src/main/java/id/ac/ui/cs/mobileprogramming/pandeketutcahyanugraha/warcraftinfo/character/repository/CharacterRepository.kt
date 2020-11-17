package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.repository

import android.content.Context
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.BlizzardAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AccountProfileSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val blizzardAPI: BlizzardAPI
) {
    fun getCharacterSummary(context: Context, onResult: (APIResponse<List<CharacterSummary>>) -> Unit) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        val accessToken = sharedPreferences.getString(context.getString(R.string.access_token_key), null)
            ?: return onResult(APIResponse.Failed(null, WarcraftInfoConstant.ACCESS_TOKEN_INVALID))

        blizzardAPI.getProfileSummary(
            WarcraftInfoConstant.NAMESPACE_PROFILE,
            WarcraftInfoConstant.LOCALE,
            accessToken
        ).enqueue(object : Callback<AccountProfileSummary> {
            override fun onResponse(
                call: Call<AccountProfileSummary>,
                response: Response<AccountProfileSummary>
            ) {
                return if (response.isSuccessful) {
                    val characterSummaryList = CharacterSummary.fromAccountProfileSummary(response.body()!!)
                    onResult(APIResponse.Success(characterSummaryList, WarcraftInfoConstant.API_RESPONSE_SUCCESS))
                } else {
                    onResult(APIResponse.Failed(null, response.code()))
                }
            }

            override fun onFailure(call: Call<AccountProfileSummary>, t: Throwable) {
                return onResult(APIResponse.Failed(null, null))
            }
        })
    }
}