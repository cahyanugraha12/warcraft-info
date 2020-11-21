package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object TaranzhiAPIModule {

    @Provides
    fun provideTaranzhiAPI(): TaranzhiAPI {
        return Retrofit.Builder()
            .baseUrl(WarcraftInfoConstant.TARANZHI_API_URI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaranzhiAPI::class.java)
    }
}