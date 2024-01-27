package com.shinjaehun.winternotes.note.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shinjaehun.winternotes.model.INoteRepository
import kotlinx.coroutines.Dispatchers

class NoteListViewModelFactory(
    private val noteRepo: INoteRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteListViewModel(noteRepo, Dispatchers.Main) as T
    }
}