package com.shinjaehun.winternotes.model

import android.util.Log
import com.shinjaehun.winternotes.common.Result
import com.shinjaehun.winternotes.common.toNote
import com.shinjaehun.winternotes.common.toNoteListFromRoomNote
import com.shinjaehun.winternotes.common.toRoomNote

private const val TAG = "NoteRepoImpl"

class NoteRepoImpl(
    val local: NoteDao
): INoteRepository {
    override suspend fun getNotes(): Result<Exception, List<Note>> {
        return getLocalNotes()
    }

    override suspend fun getNoteById(noteId: String): Result<Exception, Note> {
        return getLocalNote(noteId)
    }

    override suspend fun deleteNote(note: Note): Result<Exception, Unit> {
        return deleteLocalNote(note)
    }

    override suspend fun insertOrUpdateNote(note: Note): Result<Exception, Unit> {
        return insertOrUpdateLocalNote(note)
    }

    private suspend fun getLocalNotes(): Result<Exception, List<Note>> = Result.build {
        local.getNotes().toNoteListFromRoomNote()
    }

    private suspend fun getLocalNote(noteId: String): Result<Exception, Note> = Result.build {
        local.getNoteById(noteId).toNote
    }

    private suspend fun deleteLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        local.deleteNote(note.toRoomNote)
        Unit
    }

    private suspend fun insertOrUpdateLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        val ret = local.insertOrUpdateNote(note.toRoomNote)
        Log.i(TAG, "return value: $ret")
        Unit
    }
}