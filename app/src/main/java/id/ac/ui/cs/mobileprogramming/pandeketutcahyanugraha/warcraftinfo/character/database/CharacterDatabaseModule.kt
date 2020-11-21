package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummaryDao
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class CharacterDatabaseModule {
    @Provides
    fun provideItemDao(characterDatabase: CharacterDatabase): CharacterSummaryDao {
        return characterDatabase.characterSummaryDao()
    }

    @Provides
    @Singleton
    fun provideItemDatabase(@ApplicationContext appContext: Context): CharacterDatabase {
        return Room.databaseBuilder(
            appContext,
            CharacterDatabase::class.java,
            "character-database"
        ).build()
    }
}