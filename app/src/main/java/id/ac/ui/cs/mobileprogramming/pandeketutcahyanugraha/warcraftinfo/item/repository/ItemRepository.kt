package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auth.repository.AuthRepository
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
    private val authRepository: AuthRepository,
    @ApplicationContext private val appContext: Context
) {
    fun getItemClassList(): APIResponse<List<ItemClass>> {
        val staticToken = authRepository.getStaticToken(false)!!

        val itemClassesIndexResponse = blizzardAPI.getItemClassesIndex(
            WarcraftInfoConstant.NAMESPACE_STATIC,
            WarcraftInfoConstant.LOCALE,
            staticToken
        ).execute()

        if (!itemClassesIndexResponse.isSuccessful || itemClassesIndexResponse.body() == null) {
            return APIResponse.Failed(null, itemClassesIndexResponse.code())
        }

        return APIResponse.Success(
            ItemClass.fromItemClassesIndex(itemClassesIndexResponse.body()!!),
            WarcraftInfoConstant.API_RESPONSE_SUCCESS
        )
    }

    fun getItemById(id: Int): APIResponse<Item> {
        var item = itemDao.getById(id)

        if (item == null) {
            val staticToken = authRepository.getStaticToken(false)!!

            val itemDetailResponse = blizzardAPI.getItemById(
                id,
                WarcraftInfoConstant.NAMESPACE_STATIC,
                WarcraftInfoConstant.LOCALE,
                staticToken
            ).execute()
            if (!itemDetailResponse.isSuccessful || itemDetailResponse.body() == null) {
                return APIResponse.Failed(null, itemDetailResponse.code())
            }

            val itemMediaResponse = blizzardAPI.getItemMediaById(
                id,
                WarcraftInfoConstant.NAMESPACE_STATIC,
                WarcraftInfoConstant.LOCALE,
                staticToken
            ).execute()
            if (!itemMediaResponse.isSuccessful || itemMediaResponse.body() == null) {
                return APIResponse.Failed(null, itemMediaResponse.code())
            }

            item = Item.fromItemAndItemMedia(itemDetailResponse.body()!!, itemMediaResponse.body()!!)
            itemDao.insertAll(item)
        }

        return APIResponse.Success(item, WarcraftInfoConstant.API_RESPONSE_SUCCESS)
    }
}