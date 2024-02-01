package com.shinjaehun.winternotes.note.notedetail

import com.shinjaehun.winternotes.model.Note

sealed class NoteDetailEvent {
    data class OnDoneClick(
        val title: String,
        val subTitle: String,
        val contents: String,
        val imagePath: String,
        val color: String
        ): NoteDetailEvent()
//    data class OnDoneClick(val note: Note): NoteDetailEvent()
    object OnDeleteClick: NoteDetailEvent()
    object OnDeleteConfirmed: NoteDetailEvent()
    data class OnStart(val noteId: String): NoteDetailEvent()
    data class OnColorButtonClick(val color: String): NoteDetailEvent()
    data class OnImageButtonClick(val imagePath: String): NoteDetailEvent()
}