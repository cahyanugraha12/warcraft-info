package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ServiceComponent::class)
object BlizzardAPIModule {

    @Provides
    fun provideBlizzardAPI(): BlizzardAPI {
        return Retrofit.Builder()
            .baseUrl(WarcraftInfoConstant.BASE_API_URI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlizzardAPI::class.java)
    }
}