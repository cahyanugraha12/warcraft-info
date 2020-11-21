package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model.CharacterSummaryDao

@Database(entities = arrayOf(CharacterSummary::class), version = 1)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterSummaryDao(): CharacterSummaryDao
}