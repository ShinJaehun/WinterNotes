package com.shinjaehun.winternotes.note.notedetail

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shinjaehun.winternotes.R
import com.shinjaehun.winternotes.common.*
import com.shinjaehun.winternotes.common.BLACK
import com.shinjaehun.winternotes.common.makeToast
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
            if (binding.etNoteTitle.text.toString().trim().isEmpty()) {
                showErrorState("Note title can't be empty")
            } else if (binding.etNoteSubtitle.text.toString().trim().isEmpty()) {
                showErrorState("Note subtitle can't be empty")
            } else if (binding.etNoteContent.text.toString().trim().isEmpty()) {
                showErrorState("Note content can't be empty")
            } else {

                val gradientDrawable = binding.viewSubtitleIndicator.background as GradientDrawable
                val colorCode = String.format("#%06X", (0xFFFFFF and gradientDrawable.color!!.defaultColor!!));

                viewModel.handleEvent(
                    NoteDetailEvent.OnDoneClick(
                        binding.etNoteTitle.text.toString(),
                        binding.etNoteSubtitle.text.toString(),
                        binding.etNoteContent.text.toString(),
                        colorCode
//                    Note(
//                        //여기서 noteId를 어떻게 처리해야 할지 모르겠네...
//                    )
                    )
                )
            }
        }

        initMisc()
    }

    private fun initMisc() {
        val layoutMisc : LinearLayout = binding.misc.layoutMisc
        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(layoutMisc)

        binding.misc.tvMiscellaneous.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        val imageColor1 = binding.misc.ivColor1
        val imageColor2 = binding.misc.ivColor2
        val imageColor3 = binding.misc.ivColor3
        val imageColor4 = binding.misc.ivColor4
        val imageColor5 = binding.misc.ivColor5

        binding.misc.ivColor1.setOnClickListener {
            imageColor1.setImageResource(R.drawable.ic_done)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
            imageColor5.setImageResource(0)
            viewModel.handleEvent(
                NoteDetailEvent.OnColorButtonClick(BLACK)
            )
        }

        binding.misc.ivColor2.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(R.drawable.ic_done)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
            imageColor5.setImageResource(0)
            viewModel.handleEvent(
                NoteDetailEvent.OnColorButtonClick(PINK)
            )
        }

        binding.misc.ivColor3.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(R.drawable.ic_done)
            imageColor4.setImageResource(0)
            imageColor5.setImageResource(0)
            viewModel.handleEvent(
                NoteDetailEvent.OnColorButtonClick(DARKBLUE)
            )
        }

        binding.misc.ivColor4.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(R.drawable.ic_done)
            imageColor5.setImageResource(0)
            viewModel.handleEvent(
                NoteDetailEvent.OnColorButtonClick(YELLOW)
            )
        }

        binding.misc.ivColor5.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
            imageColor5.setImageResource(R.drawable.ic_done)
            viewModel.handleEvent(
                NoteDetailEvent.OnColorButtonClick(LIGHTBLUE)
            )
        }

//        viewModel.note.value. 뭐 이런 식으로 접근하면 안된데요...

//        activityCreateNoteBinding.misc.layoutAddImage.setOnClickListener {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            if (ContextCompat.checkSelfPermission(
//                    applicationContext,
//                    android.Manifest.permission.READ_MEDIA_IMAGES)
//                != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                    this@CreateNoteActivity,
//                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
//                    REQUEST_CODE_STORAGE_PERMISSION
//                )
//            } else {
//                selectImage()
//            }
//        }
//
//        activityCreateNoteBinding.misc.layoutAddUrl.setOnClickListener {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            showAddURLDialog()
//        }

//        if (alreadyAvailableNote != null) {
//            activityCreateNoteBinding.misc.layoutDeleteNote.visibility = View.VISIBLE
//            activityCreateNoteBinding.misc.layoutDeleteNote.setOnClickListener {
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                showDeleteNoteDialog()
//            }
//        }
    }

    private fun setSubtitleIndicatorColor(selectedNoteColor: String) {
        val gradientDrawable = binding.viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor))
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

                if (!note.color.isNullOrEmpty()){
                    when(note.color){
                        PINK->      binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color2).performClick()
                        DARKBLUE->  binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color3).performClick()
                        YELLOW->    binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color4).performClick()
                        LIGHTBLUE-> binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color5).performClick()
                        else ->     binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color1).performClick()
                    }
                    setSubtitleIndicatorColor(note.color)
                }
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

        viewModel.changedNoteColor.observe(
            this,
            Observer { noteColor ->
                setSubtitleIndicatorColor(noteColor)
                Log.i(TAG, "viewModel.changedNoteColor.observe")
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