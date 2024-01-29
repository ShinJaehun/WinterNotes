package com.shinjaehun.winternotes.note.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shinjaehun.winternotes.model.INoteRepository
import kotlinx.coroutines.Dispatchers

class NoteDetailViewModelFactory(
    private val noteRepo: INoteRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteDetailViewModel(noteRepo, Dispatchers.Main) as T
    }
}