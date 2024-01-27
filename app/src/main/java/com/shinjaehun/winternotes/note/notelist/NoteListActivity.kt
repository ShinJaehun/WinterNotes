package com.shinjaehun.winternotes.note.notelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shinjaehun.winternotes.R
import com.shinjaehun.winternotes.databinding.ActivityMainBinding
import com.shinjaehun.winternotes.databinding.ActivityNoteListBinding

private const val TAG = "MainActivity"

class NoteListActivity : AppCompatActivity() {

    private lateinit var activityNoteListBinding: ActivityNoteListBinding

    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNoteListBinding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(activityNoteListBinding.root)

    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            NoteListInjector(application)
                .provideNoteListViewModelFactory())
            .get(NoteListViewModel::class.java)

        setupAdapter()
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
}