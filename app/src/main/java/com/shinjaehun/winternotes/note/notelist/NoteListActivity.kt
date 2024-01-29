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

    private lateinit var binding: ActivityNoteListBinding
    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            NoteListInjector(application)
                .provideNoteListViewModelFactory())
            .get(NoteListViewModel::class.java)

        setupAdapter()
        observeViewModel()

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(applicationContext, NoteDetailActivity::class.java)
            intent.putExtra("noteId", "0")
            startActivity(intent)
        }

        // OnStart에서 돌리면 되는거지????
//        viewModel.handleEvent(
//            NoteListEvent.OnStart
//        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.notesRecyclerView.adapter = null
        // 이게 없으면 안되는거야?
        // memory leak이 발생하는지 여부를 알 수 있는 방법이 없을까?
    }

    override fun onStart() {
        super.onStart()
        viewModel.handleEvent( // 얘가 둘 중 하나에 가 있으면 되지 않을까?
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
                adapter.submitList(noteList)
                // note detail activity에서 DB가 update되는 것 까지 확인하고 finish()했는데
                // 다시 note list activity가 보여질 때 noteList가 갱신되지 않음
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
        binding.notesRecyclerView.layoutManager=
            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.notesRecyclerView.adapter = adapter
    }

    private fun startNoteDetailWithArgs(noteId: String?) {
        val intent = Intent(applicationContext, NoteDetailActivity::class.java)
        intent.putExtra("noteId", noteId)
        startActivity(intent)
    }

    private fun showErrorState(errorMessage: String?) = makeToast(errorMessage!!)
}