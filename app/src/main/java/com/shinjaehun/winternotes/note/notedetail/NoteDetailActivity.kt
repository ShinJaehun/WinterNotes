package com.shinjaehun.winternotes.note.notedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shinjaehun.winternotes.common.makeToast
import com.shinjaehun.winternotes.common.currentTime
import com.shinjaehun.winternotes.common.toEditable
import com.shinjaehun.winternotes.databinding.ActivityNoteDetailBinding

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

        observeViewModel()

        val noteId = intent.getStringExtra("noteId").toString()
//        Log.i(TAG, "noteId: $noteId") // NoteListActivity에서 intent로 "0"을 보냄
        viewModel.handleEvent(NoteDetailEvent.OnStart(noteId))

        binding.ivSave.setOnClickListener {
            viewModel.handleEvent(
                NoteDetailEvent.OnDoneClick(
                    binding.etNoteTitle.text.toString(),
                    binding.etNoteSubtitle.text.toString(),
                    binding.etNoteContent.text.toString()
//                    Note(
//                        //여기서 noteId를 어떻게 처리해야 할지 모르겠네...
//                    )
                )
            )
        }
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
                binding.etNoteTitle.text = note.title.toEditable()
                binding.tvDateTime.text = note.dateTime
                binding.etNoteSubtitle.text = note.subtitle.toEditable()
                binding.etNoteContent.text = note.noteContents.toEditable()

                Log.i(TAG, "viewModel.note.observe")
            }
        )

        viewModel.updated.observe(
            this,
            Observer {
                Log.i(TAG, "viewModel.update.observe")
                finish()
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