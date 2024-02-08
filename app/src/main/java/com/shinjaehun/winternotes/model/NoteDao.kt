package com.shinjaehun.winternotes.model

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY date_time DESC")
    suspend fun getNotes(): List<RoomNote>

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: String): RoomNote

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :keyword || '%' OR subtitle LIKE '%' || :keyword || '%'")
    suspend fun searchNote(keyword: String): List<RoomNote>
    @Delete
    suspend fun deleteNote(note: RoomNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: RoomNote): Long
}