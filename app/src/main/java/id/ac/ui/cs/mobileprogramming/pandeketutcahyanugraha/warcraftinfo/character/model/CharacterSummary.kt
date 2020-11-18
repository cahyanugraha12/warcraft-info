package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AccountProfileSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.Character
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.CharacterMediaSummary
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant.WarcraftInfoConstant

data class CharacterSummary(
    val id: Int,
    val avatarMediaLink: String,
    val mainMediaLink: String,
    val name: String,
    val realm: String,
    val characterClass: String,
    val race: String,
    val gender: String,
    val faction: String,
    val level: Int
) {
    companion object {
        fun fromCharacterAndMedia(character: Character, media: CharacterMediaSummary?): CharacterSummary {
            var avatarMediaLink: String = ""
            var mainMediaLink: String = ""

            if (media?.assets != null) {
                for (asset in media.assets) {
                    if (asset.key == WarcraftInfoConstant.MEDIA_AVATAR_KEY) {
                        avatarMediaLink = asset.value
                    } else if (asset.key == WarcraftInfoConstant.MEDIA_MAIN_KEY) {
                        mainMediaLink = asset.value
                    }
                }
            }

            return CharacterSummary(
                character.id,
                avatarMediaLink,
                mainMediaLink,
                character.name,
                character.realm.name,
                character.playableClass.name,
                character.playableRace.name,
                character.gender.name,
                character.faction.name,
                character.level
            )
        }
    }
}