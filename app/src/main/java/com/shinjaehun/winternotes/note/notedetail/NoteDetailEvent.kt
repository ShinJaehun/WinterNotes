package com.shinjaehun.winternotes.note.notedetail

import com.shinjaehun.winternotes.model.Note

sealed class NoteDetailEvent {
    data class OnDoneClick(
        val title: String,
        val subTitle: String,
        val contents: String
        ): NoteDetailEvent()
//    data class OnDoneClick(val note: Note): NoteDetailEvent()
    object OnDeleteClick: NoteDetailEvent()
    object OnDeleteConfirmed: NoteDetailEvent()
    data class OnStart(val noteId: String): NoteDetailEvent()
}