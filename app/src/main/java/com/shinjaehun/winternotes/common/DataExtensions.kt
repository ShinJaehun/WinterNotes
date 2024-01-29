package com.shinjaehun.winternotes.common

import android.text.Editable
import com.google.android.play.core.tasks.Task
import com.shinjaehun.winternotes.model.Note
import com.shinjaehun.winternotes.model.RoomNote
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result!!)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

internal suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

internal val RoomNote.toNote: Note
    get() = Note(
        this.noteId.toString(),
//        this.title,
//        this.dateTime,
//        this.subtitle,
        this.noteContents,
//        this.imagePath,
//        this.color,
//        this.webLink
    )

internal val Note.toRoomNote: RoomNote
    get() = RoomNote(
        this.noteId.toInt(),
//        this.title,
//        this.dateTime,
//        this.subtitle,
        this.noteContents,
//        this.imagePath,
//        this.color,
//        this.webLink
    )
// 지금 문제는 Note를 RoomNote로 변환하는 과정에서 id가 계속 자동으로 생성되어 버린다는 점!
// RoomNote를 변경해서 update일 경우 새로운 id가 추가되지 않도록 함!

internal fun List<RoomNote>.toNoteListFromRoomNote(): List<Note> = this.flatMap {
    listOf(it.toNote)
}

internal fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
