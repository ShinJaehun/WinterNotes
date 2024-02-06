package com.shinjaehun.winternotes.note.notedetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shinjaehun.winternotes.common.*
import com.shinjaehun.winternotes.common.GET_NOTE_ERROR
import com.shinjaehun.winternotes.common.currentTime
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

    private val noteColorState = MutableLiveData<String>()
    val noteColor: LiveData<String> get() = noteColorState

    private val noteImageState = MutableLiveData<String>()
    val noteImage: LiveData<String> get() = noteImageState

    private val noteImageDeletedState = MutableLiveData<Boolean>()
    val noteImageDeleted: LiveData<Boolean> get() = noteImageDeletedState

    override fun handleEvent(event: NoteDetailEvent) {
        when(event) {
            is NoteDetailEvent.OnStart -> getNote(event.noteId)
            is NoteDetailEvent.OnDeleteClick -> onDelete()
            is NoteDetailEvent.OnDoneClick -> updateNote(event.title, event.subTitle, event.contents, event.imagePath, event.color)
//            is NoteDetailEvent.OnDoneClick -> updateNote(event.note)
            is NoteDetailEvent.OnNoteColorChange -> changeNoteColor(event.color)
            is NoteDetailEvent.OnNoteImageChange -> changeNoteImage(event.imagePath)
            is NoteDetailEvent.OnNoteImageDeleteClick -> onNoteImageDelete()
            else -> {}
        }
    }

    private fun onNoteImageDelete() {
        noteImageDeletedState.value = true
        Log.i(TAG, "onNoteImageDelete()")
    }

    // 이렇게 하면 이미지 삭제 버튼을 클릭할 때 updateNote가 발생
//    private fun onNoteImageDelete() = launch {
//        val updateResult = noteRepo.insertOrUpdateNote(
//            note.value!!.copy(imagePath = "")
//        )
//        when (updateResult) {
//            is Result.Value -> updatedState.value = true
//            is Result.Error -> updatedState.value = false
//        }
//        Log.i(TAG, "onNoteImageDelete()")
//    }

    private fun changeNoteImage(imagePath: String) {
        noteImageState.value = imagePath
    }

    private fun changeNoteColor(color: String) {
//        왜 activity를 열 때마다 note를 가지고 올 때마다 changedNoteColor가 발생하는 걸까?
        Log.i(TAG, "changedNoteColor.value: ${noteColor.value}")
        Log.i(TAG, "color: $color")
        noteColorState.value = color
    }

    //    private fun updateNote(updatedNote: Note) = launch {
    private fun updateNote(title: String, subTitle: String, contents: String, imagePath: String, color: String) = launch {
//        Log.i(TAG, "insertOrUpdateNote? noteId: ${note.value!!.noteId}")

        val updateResult = noteRepo.insertOrUpdateNote(
            note.value!!
//                .copy(noteContents = updatedNote.noteContents)
                .copy(
                    title = title,
                    dateTime = currentTime(),
                    subtitle = subTitle,
                    noteContents = contents,
                    imagePath = imagePath,
                    color = color
                )
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
        if (noteId=="0" || noteId.isNullOrEmpty()) {
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
        noteState.value = Note("0","", currentTime(), "", "", "", "")
    }
}