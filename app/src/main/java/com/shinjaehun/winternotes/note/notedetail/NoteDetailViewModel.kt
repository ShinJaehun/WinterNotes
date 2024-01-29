package com.shinjaehun.winternotes.note.notedetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shinjaehun.winternotes.common.BaseViewModel
import com.shinjaehun.winternotes.common.GET_NOTE_ERROR
import com.shinjaehun.winternotes.common.Result
import com.shinjaehun.winternotes.model.INoteRepository
import com.shinjaehun.winternotes.model.Note
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private const val TAG = "NoteDetailViewModel"

class NoteDetailViewModel(
    val noteRepo: INoteRepository,
    uiContext: CoroutineContext
): BaseViewModel<NoteDetailEvent>(uiContext) {

    private val noteState = MutableLiveData<Note>()
    val note: LiveData<Note> get()= noteState

    private val deletedState = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> get() = deletedState

    private val updatedState = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> get() = updatedState

    override fun handleEvent(event: NoteDetailEvent) {
        when(event) {
            is NoteDetailEvent.OnStart -> getNote(event.noteId)
            is NoteDetailEvent.OnDeleteClick -> onDelete()
            is NoteDetailEvent.OnDoneClick -> updateNote(event.contents)
//            is NoteDetailEvent.OnDoneClick -> updateNote(event.note)
            else -> {}
        }
    }

//    private fun updateNote(updatedNote: Note) = launch {
    private fun updateNote(contents: String) = launch {
        Log.i(TAG, "insertOrUpdateNote? noteId: ${note.value!!.noteId}")

        val updateResult = noteRepo.insertOrUpdateNote(
            note.value!!
//                .copy(noteContents = updatedNote.noteContents)
                .copy(noteContents = contents)

    // NoteDetailEvent.OnStart -> getNote() -> newNote()에서 뜬금없이 note를 하나 생성하는 과정이 있었는데...
    // 그거 하지 않아서 note가 null인 건가?

//        FATAL EXCEPTION: main
//        Process: com.shinjaehun.winternotes, PID: 10741
//        java.lang.NullPointerException
//        at com.shinjaehun.winternotes.note.notedetail.NoteDetailViewModel$updateNote$1.invokeSuspend(NoteDetailViewModel.kt:41)
//        at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
//        at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
//        at android.os.Handler.handleCallback(Handler.java:942)
//        at android.os.Handler.dispatchMessage(Handler.java:99)
//        at android.os.Looper.loopOnce(Looper.java:201)
//        at android.os.Looper.loop(Looper.java:288)
//        at android.app.ActivityThread.main(ActivityThread.java:7872)
//        at java.lang.reflect.Method.invoke(Native Method)
//        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:548)
//        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:936)
//        Suppressed: kotlinx.coroutines.internal.DiagnosticCoroutineContextException: [StandaloneCoroutine{Cancelling}@85ca1c3, Dispatchers.Main]
        )

    when (updateResult) {
           is Result.Value -> updatedState.value = true
           is Result.Error -> updatedState.value = false
        }
    }

    private fun onDelete() = launch {
        val deleteResult = noteRepo.deleteNote(note.value!!)
        when(deleteResult){
            is Result.Value -> deletedState.value = true
            is Result.Error -> deletedState.value = false
        }
    }

    private fun getNote(noteId: String) = launch {
        if (noteId == "0") {
            Log.i(TAG, "new note?")
            newNote()
        }
        else {
            Log.i(TAG, "get note? noteId: $noteId")
            val noteResult = noteRepo.getNoteById(noteId)

            when (noteResult) {
                is Result.Value -> noteState.value = noteResult.value
                is Result.Error -> errorState.value = GET_NOTE_ERROR
            }
        }
    }

    private fun newNote() {
        Log.i(TAG, "new note")
        noteState.value = Note("0","")
    }
}