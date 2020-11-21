package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.auctionhouse.model.AuctionDao
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AuctionDatabaseModule {
    @Provides
    fun provideAuctionDao(auctionDatabase: AuctionDatabase): AuctionDao {
        return auctionDatabase.auctionDao()
    }

    @Provides
    @Singleton
    fun provideAuctionDatabase(@ApplicationContext appContext: Context): AuctionDatabase {
        return Room.databaseBuilder(
            appContext,
            AuctionDatabase::class.java,
            "auction-database"
        ).build()
    }
}