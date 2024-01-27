package com.shinjaehun.winternotes.note.notelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shinjaehun.winternotes.model.INoteRepository
import com.shinjaehun.winternotes.model.NoteRepoImpl
import com.shinjaehun.winternotes.model.RoomNoteDatabase

class NoteListInjector(application: Application): AndroidViewModel(application) {
    private fun getNoteRepository(): INoteRepository {
        return NoteRepoImpl(
            local=RoomNoteDatabase.getInstance(getApplication()).roomNoteDao()
        )
    }

    fun provideNoteListViewModelFactory():NoteListViewModelFactory =
        NoteListViewModelFactory(
            getNoteRepository()
        )
}