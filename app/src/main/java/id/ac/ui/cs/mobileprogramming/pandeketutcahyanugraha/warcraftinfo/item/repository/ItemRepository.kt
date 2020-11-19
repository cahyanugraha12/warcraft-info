package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.APIResponse
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.BlizzardAPI
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.Item
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.ItemClass
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import retrofit2.await
import java.util.*
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val blizzardAPI: BlizzardAPI,
    private val itemDao: ItemDao,
    @ApplicationContext private val appContext: Context
) {
    fun getItemClassList(): APIResponse<List<ItemClass>> {
        val sharedPreferences = appContext.getSharedPreferences(
            appContext.getString(R.string.common_preferences),
            Context.MODE_PRIVATE
        )
        val accessToken = sharedPreferences.getString(appContext.getString(R.string.access_token_key), null)
            ?: return APIResponse.Failed(null, WarcraftInfoConstant.ACCESS_TOKEN_INVALID)

        val itemClassesIndexResponse = blizzardAPI.getItemClassesIndex(
            WarcraftInfoConstant.NAMESPACE_STATIC,
            WarcraftInfoConstant.LOCALE,
            accessToken
        ).execute()

        if (!itemClassesIndexResponse.isSuccessful || itemClassesIndexResponse.body() == null) {
            return APIResponse.Failed(null, itemClassesIndexResponse.code())
        }

        return APIResponse.Success(
            ItemClass.fromItemClassesIndex(itemClassesIndexResponse.body()!!),
            WarcraftInfoConstant.API_RESPONSE_SUCCESS
        )
    }

    suspend fun getItemById(id: Int): APIResponse<Item> {
        var item = itemDao.getById(id)

        if (item == null) {
            val sharedPreferences = appContext.getSharedPreferences(
                appContext.getString(R.string.common_preferences),
                Context.MODE_PRIVATE
            )
            val accessToken = sharedPreferences.getString(appContext.getString(R.string.access_token_key), null)
                ?: return APIResponse.Failed(null, WarcraftInfoConstant.ACCESS_TOKEN_INVALID)

            val itemDetailResponseDeferred = GlobalScope.async(Dispatchers.IO) {
                blizzardAPI.getItemById(
                    id,
                    WarcraftInfoConstant.NAMESPACE_STATIC,
                    WarcraftInfoConstant.LOCALE,
                    accessToken
                ).execute()
            }
            val itemMediaResponseDeferred = GlobalScope.async(Dispatchers.IO) {
                blizzardAPI.getItemMediaById(
                    id,
                    WarcraftInfoConstant.NAMESPACE_STATIC,
                    WarcraftInfoConstant.LOCALE,
                    accessToken
                ).execute()
            }
            val itemDetailResponse = itemDetailResponseDeferred.await()
            if (!itemDetailResponse.isSuccessful || itemDetailResponse.body() == null) {
                return APIResponse.Failed(null, itemDetailResponse.code())
            }

            val itemMediaResponse = itemMediaResponseDeferred.await()
            if (!itemMediaResponse.isSuccessful || itemMediaResponse.body() == null) {
                return APIResponse.Failed(null, itemMediaResponse.code())
            }

            item = Item.fromItemAndItemMedia(itemDetailResponse.body()!!, itemMediaResponse.body()!!)
            itemDao.insertAll(item)
        }

        return APIResponse.Success(item, WarcraftInfoConstant.API_RESPONSE_SUCCESS)
    }
}