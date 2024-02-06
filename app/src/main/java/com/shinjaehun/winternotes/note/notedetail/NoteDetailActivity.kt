package com.shinjaehun.winternotes.note.notedetail

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import com.shinjaehun.winternotes.common.makeToast
import com.shinjaehun.winternotes.common.toEditable
import com.shinjaehun.winternotes.databinding.ActivityNoteDetailBinding

private const val TAG = "NoteDetailActivity"
class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var viewModel: NoteDetailViewModel

    private var selectedImagePath: String = ""

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
                        selectedImagePath, // 일단 임시로 이렇게는 해놨는데... 이렇게 해도 되는건지는 모르겠음.
                        colorCode, //colorCode도 사실 저렇게 전역변수로 처리하면 될텐데 그렇게 하면 안되는거지?
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
                NoteDetailEvent.OnNoteColorChange(ColorBLACK)
            )
        }

        binding.misc.ivColor2.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(R.drawable.ic_done)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
            imageColor5.setImageResource(0)
            viewModel.handleEvent(
                NoteDetailEvent.OnNoteColorChange(ColorPINK)
            )
        }

        binding.misc.ivColor3.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(R.drawable.ic_done)
            imageColor4.setImageResource(0)
            imageColor5.setImageResource(0)
            viewModel.handleEvent(
                NoteDetailEvent.OnNoteColorChange(ColorDARKBLUE)
            )
        }

        binding.misc.ivColor4.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(R.drawable.ic_done)
            imageColor5.setImageResource(0)
            viewModel.handleEvent(
                NoteDetailEvent.OnNoteColorChange(ColorYELLOW)
            )
        }

        binding.misc.ivColor5.setOnClickListener {
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
            imageColor5.setImageResource(R.drawable.ic_done)
            viewModel.handleEvent(
                NoteDetailEvent.OnNoteColorChange(ColorLIGHTBLUE)
            )
        }

//        viewModel.note.value. 뭐 이런 식으로 접근하면 안된데요...

        binding.misc.layoutAddImage.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            // Register ActivityResult handler
//            val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
//
//            }

//            // Permission request logic
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_VISUAL_USER_SELECTED))
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
//            } else {
//                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
//            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
//            } else {
//                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
//                (ContextCompat.checkSelfPermission(applicationContext, READ_MEDIA_IMAGES) == PERMISSION_GRANTED ||
//                        ContextCompat.checkSelfPermission(applicationContext, READ_MEDIA_VIDEO) == PERMISSION_GRANTED
//                        )
//            ) {
//                // Full access on Android 13 (API level 33) or higher
//            } else if (
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
//                ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED
//            ) {
//                // Partial access on Android 14 (API level 34) or higher
//            }  else if (ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
//                // Full access up to Android 12 (API level 32)
//            } else {
//                // Access denied
//            }

            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this@NoteDetailActivity,
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            } else {
                selectImage()
            }
        }

        binding.misc.layoutAddUrl.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showAddURLDialog()
        }

//        if (alreadyAvailableNote != null) {
//            activityCreateNoteBinding.misc.layoutDeleteNote.visibility = View.VISIBLE
//            activityCreateNoteBinding.misc.layoutDeleteNote.setOnClickListener {
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                showDeleteNoteDialog()
//            }
//        }
    }

    private fun showAddURLDialog() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var fileName = ""
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (data != null) {
                val selectedImageUri = data.data
                // 이건 official document에서 보장하는 내용
//                selectedImageUri.let { returnUri ->
//                    returnUri?.let { contentResolver.query(it, null, null, null, null) }
//                }?.use { cursor ->
//                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
//                    cursor.moveToFirst()
//                    Log.i(TAG, "${cursor.getString(nameIndex)}")
//                    Log.i(TAG, "${cursor.getString(sizeIndex)}")
//                    fileName = cursor.getString(nameIndex)
//                }

//                Log.i(TAG, "onActivityResult: $selectedImageUri")
                if (selectedImageUri != null) {
//                    Log.i(TAG, "onActivityResult: ${selectedImageUri!!.path!!.substring(selectedImageUri!!.path!!.lastIndexOf('/') + 1)}")

                    try {
                        // private storage issue 때문에 이미지 복사
//                        FileUtils.application = application
//                        FileUtils.cRes = contentResolver
//
//                        val inputStream = FileUtils.getInputStream(selectedImageUri)
//                        val path = this.getExternalFilesDir(null)
//                        val folder = File(path, "images")
//                        folder.mkdirs()
//                        val outputFile = File(folder, fileName)
//                        FileUtils.copyStreamToFile(inputStream!!, outputFile)
//
//                        binding.ivNote.setImageURI(Uri.fromFile(outputFile))
//                        binding.ivNote.visibility = View.VISIBLE
//                        binding.ivDeleteImage.visibility = View.VISIBLE
//
//                        selectedImagePath = outputFile.path
//
//                        viewModel.handleEvent(
//                            NoteDetailEvent.OnNoteImageChange(selectedImagePath)
//                        )

                        showImage(selectedImagePath)
//                        binding.ivNote.setImageURI(selectedImageUri)
//                        binding.ivNote.visibility = View.VISIBLE
//                        binding.ivDeleteImage.visibility = View.VISIBLE

                        viewModel.handleEvent(
                            NoteDetailEvent.OnNoteImageChange(getPathFromUri(selectedImageUri))
                        )

                        selectedImagePath = getPathFromUri(selectedImageUri)
                    } catch (e: Exception) {
                        showErrorState(e.toString())
                    }
                }
            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String {
        // contentResolver와 cursor에 대해 공부 필요!
        val filePath: String
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path.toString()
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private fun selectImage() {

        // private storage issue 때문에 이미지 복사
//        Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
//            it.type="image/*"
//            startActivityForResult(it, REQUEST_CODE_SELECT_IMAGE)
//        }

        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_SELECT_IMAGE) }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImage(path: String){
        binding.ivNote.setImageURI(Uri.parse(path))
        binding.ivNote.visibility = View.VISIBLE
        binding.ivDeleteImage.visibility = View.VISIBLE
        binding.ivDeleteImage.setOnClickListener {
            viewModel.handleEvent(
                NoteDetailEvent.OnNoteImageDeleteClick
            )
        }
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
//                Log.i(TAG, "1 $selectedImagePath")

                binding.etNoteTitle.text = note.title.toEditable()
                binding.tvDateTime.text = note.dateTime
                binding.etNoteSubtitle.text = note.subtitle.toEditable()
                binding.etNoteContent.text = note.noteContents.toEditable()

                if(!note.imagePath.isNullOrEmpty()) {
                    selectedImagePath= note.imagePath.toString() // 이렇게 해도 되는 건가요????????
                    showImage(note.imagePath)
                }

                if (!note.color.isNullOrEmpty()){
                    when(note.color){
                        ColorPINK->      binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color2).performClick()
                        ColorDARKBLUE->  binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color3).performClick()
                        ColorYELLOW->    binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color4).performClick()
                        ColorLIGHTBLUE-> binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color5).performClick()
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
//                Log.i(TAG, "2 $selectedImagePath")

                Log.i(TAG, "viewModel.update.observe")
                finish()
            }
        )

        viewModel.noteImage.observe(
            this,
            Observer{ imagePath ->
                if (!imagePath.isNullOrEmpty()) {
                    selectedImagePath = imagePath // 얘를 이렇게 저장하면 안되는거?
//                Log.i(TAG, "3 $selectedImagePath")
                    showImage(imagePath)
                } else {
                    binding.ivNote.visibility = View.GONE
                    binding.ivDeleteImage.visibility = View.GONE
                }
                Log.i(TAG, "viewModel.noteImage.observe")
            }
        )

        viewModel.noteColor.observe(
            this,
            Observer { noteColor ->
//                Log.i(TAG, "4 $selectedImagePath")

                setSubtitleIndicatorColor(noteColor)
                Log.i(TAG, "viewModel.noteColor.observe")
            }
        )

        viewModel.deleted.observe(
            this,
            Observer {
                Log.i(TAG, "viewModel.deleted.observe")
            }
        )

        viewModel.noteImageDeleted.observe(
            this,
            Observer {
                selectedImagePath = ""
                binding.ivNote.visibility = View.GONE
                binding.ivDeleteImage.visibility = View.GONE
                Log.i(TAG, "viewModel.noteImageDeleted.observe")
            }
        )
    }

    private fun showErrorState(errorMessage: String?) = makeToast(errorMessage!!)
}