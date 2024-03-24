package com.shinjaehun.winternotes.note.notelist

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.Global
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shinjaehun.winternotes.databinding.ActivityNoteListBinding
import com.shinjaehun.winternotes.common.makeToast
import com.shinjaehun.winternotes.model.Note
import com.shinjaehun.winternotes.note.notedetail.NoteDetailActivity
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*

private const val TAG = "MainActivity"

class NoteListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteListBinding
    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter


    companion object {
        var screenHeight = 0
        var screenWidth = 0

        var snowList: ArrayList<SnowFlake> = ArrayList()

        var isNotPaused = true

        var job1: Job = Job()

        const val disappear_margin = 32				// pixels from each border where objects disappear
        const val flake_TX: Float = 1f // max. sec. of flake's constant X-movement on fluttering
        const val flake_XperY: Float = 2f // fluttering movement's max. vx/vy ratio
        var refresh_FperS = 100f					// initial frames/second, recalculated.
        var flake_speed 	= 0.3f				// flake speed in pixel/frame
    }

    private var timer: Timer? = null
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

        binding.etSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cancelTimer()
            }

            override fun afterTextChanged(s: Editable?) {
                timer = Timer()
                timer!!.schedule(object: TimerTask(){
                    override fun run() {
                        if (!s.toString().isEmpty()) {
                            viewModel.handleEvent(
                                NoteListEvent.OnSearchTextChange(s.toString())
                            )
                        } else {
                            viewModel.handleEvent(
                                NoteListEvent.OnStart
                            )
                        }
                    }
                }, 500)
            }
        })

        val wm = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            screenWidth = windowMetrics.bounds.width() - insets.left - insets.right
            screenHeight = windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val point = Point()
            wm.defaultDisplay.getRealSize(point)
            screenWidth = point.x
            screenHeight = point.y
        }
        setUpSnowEffect()

//        job1 = lifecycleScope.launch {
        job1 = GlobalScope.launch {
            withContext(Dispatchers.Main) {
                updateSnowFlakes(10L)
            }
        }
    }

    private fun cancelTimer() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.notesRecyclerView.adapter = null
        // 이게 없으면 안되는거야?
        // memory leak이 발생하는지 여부를 알 수 있는 방법이 없을까?
    }

    override fun onStart() {
        super.onStart()
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

        viewModel.searchText.observe(
            this,
            Observer { noteList ->
                adapter.updateList(noteList)
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


    private fun setUpSnowEffect() {
        // generate 200 snow flake
        val container: ViewGroup = window.decorView as ViewGroup
        for (i in 0 until 30) {
            snowList.add(
                SnowFlake(
                    baseContext,
                    container,
                    screenWidth.toFloat(),
                    screenHeight.toFloat(),
                    false,
                )
            )
        }

        Log.i(TAG, "the size of snowList: ${snowList.size}")
    }

    private suspend fun updateSnowFlakes(delay_refresh: Long){
        while (isNotPaused) {
            for (snow: SnowFlake in snowList){
                snow.update()
            }
            delay(delay_refresh)
        }
    }
}