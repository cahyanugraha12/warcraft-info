package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.CharacterAPIDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.CharacterMediaSummaryAPIDao
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant

@Entity(tableName = "character_summary")
data class CharacterSummary(
    @PrimaryKey val id: Int,
    val avatarMediaLink: String,
    val mainMediaLink: String,
    val name: String,
    val realm: String,
    val realmSlug: String,
    val characterClass: String,
    val race: String,
    val gender: String,
    val faction: String,
    val level: Int
) {
    companion object {
        fun fromCharacterAndMedia(character: CharacterAPIDao, media: CharacterMediaSummaryAPIDao): CharacterSummary {
            var avatarMediaLink: String = ""
            var mainMediaLink: String = ""

            if (media.assets != null) {
                // Handle US-Oceania response variant
                for (asset in media.assets) {
                    if (asset.key == WarcraftInfoConstant.MEDIA_AVATAR_KEY) {
                        avatarMediaLink = asset.value
                    } else if (asset.key == WarcraftInfoConstant.MEDIA_MAIN_KEY) {
                        mainMediaLink = asset.value
                    }
                }
            } else if (media.avatarUrl != null && media.mainUrl != null) {
                // Handle US-North America response variant
                avatarMediaLink = media.avatarUrl
                mainMediaLink = media.mainUrl
            }

            return CharacterSummary(
                character.id,
                avatarMediaLink,
                mainMediaLink,
                character.name,
                character.realm.name,
                character.realm.slug,
                character.playableClass.name,
                character.playableRace.name,
                character.gender.name,
                character.faction.name,
                character.level
            )
        }
    }
}