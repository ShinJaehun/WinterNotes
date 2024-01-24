package com.shinjaehun.winternotes.model

import com.shinjaehun.winternotes.common.Result

interface INoteRepository {
    suspend fun getNotes(): Result<Exception, List<Note>>
    suspend fun getNoteById(noteId: Int):Result<Exception, Note>
    suspend fun deleteNote(note: Note): Result<Exception, Unit>
    suspend fun insertOrUpdateNote(note: Note): Result<Exception, Unit>
}