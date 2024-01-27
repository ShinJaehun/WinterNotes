package com.shinjaehun.winternotes.note.notelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shinjaehun.winternotes.common.BaseViewModel
import com.shinjaehun.winternotes.common.GET_NOTES_ERROR
import com.shinjaehun.winternotes.common.Result
import com.shinjaehun.winternotes.model.INoteRepository
import com.shinjaehun.winternotes.model.Note
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private const val TAG = "NoteListViewModel"

class NoteListViewModel(
    val noteRepo: INoteRepository,
    uiContext: CoroutineContext
): BaseViewModel<NoteListEvent>(uiContext) {

    private val noteListState = MutableLiveData<List<Note>>()
    val noteList: LiveData<List<Note>> get() = noteListState

    private val editNoteState = MutableLiveData<String>()
    val editNote: LiveData<String> get() = editNoteState

    override fun handleEvent(event: NoteListEvent) {
        when(event) {
//            is NoteListEvent.OnNewNoteClick -> TODO()
            is NoteListEvent.OnStart ->getNotes()
            is NoteListEvent.OnNoteItemClick -> editNote(event.position)
            else -> {}
        }
    }

    private fun editNote(position: Int) {
        Log.i(TAG, noteList.value!![position].noteId.toString())
        editNoteState.value = noteList.value!![position].noteId.toString() // noteId가 Int이기 때문?? 그렇다면 editNoteState 자료형을 바꿔야 하는 거 아닐까?
    }

    private fun getNotes() = launch {
        val notesResult = noteRepo.getNotes()

        when(notesResult) {
            is Result.Value -> noteListState.value = notesResult.value
            is Result.Error -> errorState.value = GET_NOTES_ERROR
        }
    }
}