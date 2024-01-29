package com.shinjaehun.winternotes.note.notedetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shinjaehun.winternotes.model.INoteRepository
import com.shinjaehun.winternotes.model.NoteRepoImpl
import com.shinjaehun.winternotes.model.RoomNoteDatabase

class NoteDetailInjector(application: Application): AndroidViewModel(application) {
    private fun getNoteRepository(): INoteRepository {
        return NoteRepoImpl(
            local = RoomNoteDatabase.getInstance(getApplication()).roomNoteDao()
        )
    }

    fun provideNoteDetailViewModelFactory(): NoteDetailViewModelFactory=
        NoteDetailViewModelFactory(
            getNoteRepository()
        )
}