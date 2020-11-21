package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.item.model.ItemDao
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class ItemDatabaseModule {
    @Provides
    fun provideItemDao(itemDatabase: ItemDatabase): ItemDao {
        return itemDatabase.itemDao()
    }

    @Provides
    @Singleton
    fun provideItemDatabase(@ApplicationContext appContext: Context): ItemDatabase {
        return Room.databaseBuilder(
            appContext,
            ItemDatabase::class.java,
            "item-database"
        ).build()
    }
}