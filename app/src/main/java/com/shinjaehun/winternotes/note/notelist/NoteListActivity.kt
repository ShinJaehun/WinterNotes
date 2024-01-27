package com.shinjaehun.winternotes.note.notelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shinjaehun.winternotes.databinding.ActivityNoteListBinding
import com.shinjaehun.winternotes.common.makeToast
import com.shinjaehun.winternotes.note.notedetail.NoteDetailActivity

private const val TAG = "MainActivity"

class NoteListActivity : AppCompatActivity() {

    private lateinit var activityNoteListBinding: ActivityNoteListBinding

    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNoteListBinding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(activityNoteListBinding.root)

        activityNoteListBinding.fabAddNote.setOnClickListener {
            startActivity(Intent(applicationContext, NoteDetailActivity::class.java))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        activityNoteListBinding.notesRecyclerView.adapter = null
        // 이게 없으면 안되는거야?
        // memory leak이 발생하는지 여부를 알 수 있는 방법이 없을까?
    }
    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            NoteListInjector(application)
                .provideNoteListViewModelFactory())
            .get(NoteListViewModel::class.java)

        setupAdapter()
        observeViewModel()

        viewModel.handleEvent(
            NoteListEvent.OnStart
        )
    }

    private fun observeViewModel() {
        viewModel.error.observe(
            this,
            Observer { errorMessage ->
                showErrorState(errorMessage)
            }
        )

        viewModel.noteList.observe(
            this,
            Observer { noteList ->
                adapter.updateList(noteList)

            }
        )

        viewModel.editNote.observe(
            this,
            Observer { noteId ->
                startNoteDetailWithArgs(noteId)
            }
        )
    }

    private fun setupAdapter() {
        adapter = NoteListAdapter()
        adapter.event.observe(
            this,
            Observer {
                viewModel.handleEvent(it)
            }
        )
        activityNoteListBinding.notesRecyclerView.layoutManager=
            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        activityNoteListBinding.notesRecyclerView.adapter = adapter
    }

    private fun startNoteDetailWithArgs(noteId: String?) {
        Log.i(TAG, "이건 뭐... $noteId")
    }

    private fun showErrorState(errorMessage: String?) = makeToast(errorMessage!!)
}