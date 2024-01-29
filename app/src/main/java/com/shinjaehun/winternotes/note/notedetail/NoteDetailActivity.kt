package com.shinjaehun.winternotes.note.notedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shinjaehun.winternotes.R
import com.shinjaehun.winternotes.common.makeToast
import com.shinjaehun.winternotes.common.toEditable
import com.shinjaehun.winternotes.databinding.ActivityNoteDetailBinding
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "NoteDetailActivity"
class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var viewModel: NoteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            NoteDetailInjector(application).provideNoteDetailViewModelFactory())
            .get(NoteDetailViewModel::class.java)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvDateTime.text = SimpleDateFormat(
            "yyyy MMMM dd, EEEE, HH:mm a",
            Locale.getDefault()).format(Date())

        binding.ivSave.setOnClickListener {
            viewModel.handleEvent(
                NoteDetailEvent.OnDoneClick(
                    binding.etNoteContent.text.toString()
                )
            )
//            saveNote()
        }

        observeViewModel()

        val noteId = intent.getStringExtra("noteId").toString()
        viewModel.handleEvent(
            NoteDetailEvent.OnStart(
                noteId
            )
        )

    }

    private fun saveNote() {
        TODO("Not yet implemented")
    }

    private fun observeViewModel() {
        viewModel.error.observe(
            this,
            Observer { errorMessage ->
                showErrorState(errorMessage)
            }
        )

        viewModel.note.observe(
            this,
            Observer { note ->
//                binding.etNoteContent.text = note.noteContents.toEditable()
                Log.i(TAG, "viewModel.note.observe")
            }
        )

        viewModel.updated.observe(
            this,
            Observer {
                Log.i(TAG, "viewModel.update.observe")
            }
        )

        viewModel.deleted.observe(
            this,
            Observer {
                Log.i(TAG, "viewModel.deleted.observe")
            }
        )
    }

    private fun showErrorState(errorMessage: String?) = makeToast(errorMessage!!)

}