package com.shinjaehun.winternotes.note.notedetail

sealed class NoteDetailEvent {
    data class OnDoneClick(
        val title: String,
        val subTitle: String,
        val contents: String,
        val imagePath: String?,
        val color: String?,
        val webLink: String?
        ): NoteDetailEvent()
//    data class OnDoneClick(val note: Note): NoteDetailEvent()
    object OnDeleteClick: NoteDetailEvent()
    object OnDeleteConfirmed: NoteDetailEvent()
    data class OnStart(val noteId: String): NoteDetailEvent()
    data class OnNoteColorChange(val color: String): NoteDetailEvent()
    data class OnNoteImageChange(val imagePath: String?): NoteDetailEvent()
    data class OnWebLinkChange(val webLink: String?): NoteDetailEvent()
    object OnNoteImageDeleteClick: NoteDetailEvent()
    object OnNoteURLDeleteClick: NoteDetailEvent()
}