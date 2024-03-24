package com.shinjaehun.winternotes.note.notelist

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shinjaehun.winternotes.R
import com.shinjaehun.winternotes.common.makeToast
import com.shinjaehun.winternotes.databinding.FragmentNoteDetailBinding
import com.shinjaehun.winternotes.databinding.FragmentNoteListBinding


import java.util.*

private const val TAG = "NoteListView"

class NoteListView : Fragment() {

    private lateinit var binding: FragmentNoteListBinding
    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter


//    companion object {
//        var screenHeight = 0
//        var screenWidth = 0
//
//        var snowList: ArrayList<SnowFlake> = ArrayList()
//
//        var isNotPaused = true
//
//        var job1: Job = Job()
//
//        const val disappear_margin = 32				// pixels from each border where objects disappear
//        const val flake_TX: Float = 1f // max. sec. of flake's constant X-movement on fluttering
//        const val flake_XperY: Float = 2f // fluttering movement's max. vx/vy ratio
//        var refresh_FperS = 100f					// initial frames/second, recalculated.
//        var flake_speed 	= 0.3f				// flake speed in pixel/frame
//    }

    private var timer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.i(TAG, "cooooool~~~~~~~")
//            findNavController().popBackStack()
            activity?.finish()
        }

        binding = FragmentNoteListBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.notesRecyclerView.adapter = null
        // 이게 없으면 안되는거야?
        // memory leak이 발생하는지 여부를 알 수 있는 방법이 없을까?
    }

    override fun onStart() {
        super.onStart()

        viewModel = ViewModelProvider(
            this,
            NoteListInjector(requireActivity().application).provideNoteListViewModelFactory()
        ).get(
            NoteListViewModel::class.java
        )

        setupAdapter()
        observeViewModel()

        binding.fabAddNote.setOnClickListener {
            val direction = NoteListViewDirections.actionNoteListViewToNoteDetailView("")
            findNavController().navigate(direction)
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

        viewModel.handleEvent(
            NoteListEvent.OnStart
        )
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityNoteListBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        viewModel = ViewModelProvider(
//            this,
//            NoteListInjector(application)
//                .provideNoteListViewModelFactory())
//            .get(NoteListViewModel::class.java)
//
//        setupAdapter()
//        observeViewModel()
//
//        binding.fabAddNote.setOnClickListener {
//            val intent = Intent(applicationContext, NoteDetailActivity::class.java)
//            intent.putExtra("noteId", "0")
//            startActivity(intent)
//        }
//
//        binding.etSearch.addTextChangedListener(object: TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                cancelTimer()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                timer = Timer()
//                timer!!.schedule(object: TimerTask(){
//                    override fun run() {
//                        if (!s.toString().isEmpty()) {
//                            viewModel.handleEvent(
//                                NoteListEvent.OnSearchTextChange(s.toString())
//                            )
//                        } else {
//                            viewModel.handleEvent(
//                                NoteListEvent.OnStart
//                            )
//                        }
//                    }
//                }, 500)
//            }
//        })
//
////        val wm = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
////            val windowMetrics = wm.currentWindowMetrics
////            val insets = windowMetrics.windowInsets
////                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
////            screenWidth = windowMetrics.bounds.width() - insets.left - insets.right
////            screenHeight = windowMetrics.bounds.height() - insets.bottom - insets.top
////        } else {
////            val point = Point()
////            wm.defaultDisplay.getRealSize(point)
////            screenWidth = point.x
////            screenHeight = point.y
////        }
////        setUpSnowEffect()
////
////        job1 = lifecycleScope.launch {
////            withContext(Dispatchers.Main) {
////                updateSnowFlakes(10L)
////            }
////        }
//    }

    private fun cancelTimer() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    private fun observeViewModel() {
        viewModel.error.observe(
            viewLifecycleOwner,
            Observer { errorMessage ->
                showErrorState(errorMessage)
            }
        )

        viewModel.noteList.observe(
            viewLifecycleOwner,
            Observer { noteList ->
                adapter.updateList(noteList)
            }
        )

        viewModel.editNote.observe(
            viewLifecycleOwner,
            Observer { noteId ->
                startNoteDetailWithArgs(noteId)
            }
        )

        viewModel.searchText.observe(
            viewLifecycleOwner,
            Observer { noteList ->
                adapter.updateList(noteList)
            }
        )

//        requireActivity().onBackPressedDispatcher.addCallback(this) {
//            findNavController().popBackStack()
//        }

//        val callback: OnBackPressedCallback = object :
//            OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                this.remove()
//                activity?.onBackPressed()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setupAdapter() {
        adapter = NoteListAdapter()
        adapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.handleEvent(it)
            }
        )
        binding.notesRecyclerView.layoutManager=
            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.notesRecyclerView.adapter = adapter
    }

    private fun startNoteDetailWithArgs(noteId: String) = findNavController().navigate(
        NoteListViewDirections.actionNoteListViewToNoteDetailView(noteId)
    )

    private fun showErrorState(errorMessage: String?) = makeToast(errorMessage!!)


//    private fun setUpSnowEffect() {
//        // generate 200 snow flake
//        val container: ViewGroup = window.decorView as ViewGroup
//        for (i in 0 until 30) {
//            snowList.add(
//                SnowFlake(
//                    baseContext,
//                    container,
//                    screenWidth.toFloat(),
//                    screenHeight.toFloat(),
//                    false,
//                )
//            )
//        }
//
//        Log.i(TAG, "the size of snowList: ${snowList.size}")
//    }
//
//    private suspend fun updateSnowFlakes(delay_refresh: Long){
//        while (isNotPaused) {
//            for (snow: SnowFlake in snowList){
//                snow.update()
//            }
//            delay(delay_refresh)
//        }
//    }
}