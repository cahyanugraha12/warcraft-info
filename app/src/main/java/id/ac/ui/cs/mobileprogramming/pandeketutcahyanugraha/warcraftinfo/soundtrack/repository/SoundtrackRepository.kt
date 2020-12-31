package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.soundtrack.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.TaranzhiAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.SoundtrackMetadataDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject

class SoundtrackRepository @Inject constructor(
    private val taranzhiAPI: TaranzhiAPI,
    @ApplicationContext private val appContext: Context
) {
    suspend fun fetchSoundtrackMetadata(): APIResponse<List<SoundtrackMetadataDao>> {
        val soundtrackMetadataResponse = GlobalScope.async(Dispatchers.IO) {
            taranzhiAPI.getSoundtrackMetadata().execute()
        }.await()

        if (!soundtrackMetadataResponse.isSuccessful || soundtrackMetadataResponse.body() == null) {
            return APIResponse.Failed(null, soundtrackMetadataResponse.code())
        }

        return APIResponse.Success(
            soundtrackMetadataResponse.body(),
            WarcraftInfoConstant.API_RESPONSE_SUCCESS
        )
    }

    fun setSoundtrackMetadata(soundtrackMetadata: List<SoundtrackMetadataDao>) {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val gson = Gson()
        val stringifiedSoundtrackMetadata = gson.toJson(soundtrackMetadata)
        sharedPreferences
            .edit()
            .putString(WarcraftInfoConstant.SOUNDTRACK_METADATA_KEY, stringifiedSoundtrackMetadata)
            .apply()
    }

    fun getSoundtrackMetadataList(): List<SoundtrackMetadataDao> {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val stringifiedSoundtrackMetadata = sharedPreferences.getString(
            WarcraftInfoConstant.SOUNDTRACK_METADATA_KEY,
            null
        )
        val gson = Gson()
        val type = object : TypeToken<List<SoundtrackMetadataDao>>() {}.type
        return gson.fromJson(stringifiedSoundtrackMetadata!!, type)
    }

    fun getSoundtrackMetadataByIndex(index: Int): SoundtrackMetadataDao? {
        val soundtrackMetadataList = getSoundtrackMetadataList()
        return soundtrackMetadataList.getOrNull(index)
    }

    fun setSoundtrackIndex(index: Int) {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )

        sharedPreferences
            .edit()
            .putInt(WarcraftInfoConstant.SOUNDTRACK_INDEX_KEY, index)
            .apply()
    }

    fun getSoundtrackIndex(): Int? {
        val sharedPreferences = appContext.getSharedPreferences(
            WarcraftInfoConstant.COMMON_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val index = sharedPreferences.getInt(WarcraftInfoConstant.SOUNDTRACK_INDEX_KEY, -1)
        if (index == -1) {
            return null
        }

        return index
    }
}