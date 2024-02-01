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
            // fab을 클릭하면 NoteDetailActivity를 실행시키기만 하면 되므로(다른 로직 필요 없음) 걍 activity에서 event로 처리
            is NoteListEvent.OnStart ->getNotes()
            is NoteListEvent.OnNoteItemClick -> editNote(event.position)
            else -> {}
        }
    }

    private fun editNote(position: Int) {
        Log.i(TAG, noteList.value!![position].noteId)
        editNoteState.value = noteList.value!![position].noteId
    }

    private fun getNotes() = launch {
        val notesResult = noteRepo.getNotes()

        when(notesResult) {
            is Result.Value -> noteListState.value = notesResult.value
            is Result.Error -> errorState.value = GET_NOTES_ERROR
        }
    }
}