package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.model

import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api.dao.AccountProfileSummary

data class CharacterSummary(
    val id: Int,
    val name: String,
    val realm: String,
    val characterClass: String,
    val race: String,
    val gender: String,
    val faction: String,
    val level: Int
) {
    companion object {
        fun fromAccountProfileSummary(accountProfileSummary: AccountProfileSummary): List<CharacterSummary> {
            val characterSummaryList = mutableListOf<CharacterSummary>()

            for (account in accountProfileSummary.wowAccounts) {
                for (character in account.characters) {
                    val characterSummary = CharacterSummary(
                        character.id,
                        character.name,
                        character.realm.name,
                        character.playableClass.name,
                        character.playableRace.name,
                        character.gender.name,
                        character.faction.name,
                        character.level
                    )

                    characterSummaryList.add(characterSummary)
                }
            }

            return characterSummaryList
        }
    }
}