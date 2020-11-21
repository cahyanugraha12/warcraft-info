package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model

import androidx.room.*

@Dao
interface CharacterSummaryDao {
    @Query("SELECT * FROM character_summary")
    fun getAll(): List<CharacterSummary>

    @Query("SELECT * FROM character_summary WHERE id = :id")
    fun getById(id: Int): CharacterSummary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg characterSummaries: CharacterSummary)

    @Delete
    fun delete(characterSummary: CharacterSummary)
}