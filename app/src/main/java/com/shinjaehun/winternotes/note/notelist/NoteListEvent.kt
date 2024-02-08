package com.shinjaehun.winternotes.note.notelist

import com.shinjaehun.winternotes.note.notedetail.NoteDetailEvent

sealed class NoteListEvent {
    data class OnNoteItemClick(val position: Int): NoteListEvent()
    object OnNewNoteClick: NoteListEvent()
    object OnStart: NoteListEvent()
    data class OnSearchTextChange(val searchKeyword: String): NoteListEvent()
}