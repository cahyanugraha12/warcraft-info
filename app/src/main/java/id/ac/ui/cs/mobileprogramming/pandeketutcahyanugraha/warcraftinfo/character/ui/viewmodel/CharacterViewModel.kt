package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.ui.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.character.repository.CharacterRepository

class CharacterViewModel @ViewModelInject constructor(
    private val characterRepository: CharacterRepository,
    application: Application
) : AndroidViewModel(application) {
    val context = application.applicationContext
    val characterSummaryList = characterRepository.getCharacterSummary(context, onResult)
}