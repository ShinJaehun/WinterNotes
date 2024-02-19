package com.shinjaehun.winternotes.note.notedetail

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shinjaehun.winternotes.R
import com.shinjaehun.winternotes.common.*
import com.shinjaehun.winternotes.common.makeToast
import com.shinjaehun.winternotes.common.toEditable
import com.shinjaehun.winternotes.databinding.ActivityNoteDetailBinding
import java.io.File

private const val TAG = "NoteDetailActivity"
class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var viewModel: NoteDetailViewModel

//    private var selectedImagePath: String? = null

//    private val readExternal=READ_EXTERNAL_STORAGE
//    private val readVideo=READ_MEDIA_VIDEO
//    private val readImages=READ_MEDIA_IMAGES
//    private val permissions= arrayOf(
//        readVideo,readImages
//    )

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

                val selectedImagePath: String? = binding.ivNote.tag as String?
                Log.i(TAG, "$selectedImagePath")

                val gradientDrawable = binding.viewSubtitleIndicator.background as GradientDrawable
                val colorCode = String.format("#%06X", (0xFFFFFF and gradientDrawable.color!!.defaultColor!!));
                // colorCode는 null 값을 가질 수 없음!

                val webUrl = binding.tvWebUrl.text.toString().ifEmpty {
                    null
                }

                viewModel.handleEvent(
                    NoteDetailEvent.OnDoneClick(
                        binding.etNoteTitle.text.toString(),
                        binding.etNoteSubtitle.text.toString(),
                        binding.etNoteContent.text.toString(),
                        selectedImagePath, // 일단 임시로 이렇게는 해놨는데... 이렇게 해도 되는건지는 모르겠음.
                        colorCode, //colorCode도 사실 저렇게 전역변수로 처리하면 될텐데 그렇게 하면 안되는거지?
                         // 얘는 빈 값일때 그냥 null로 처리했으면 좋겠는데...
                        webUrl
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

        binding.misc.layoutAddImage.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            if (ContextCompat.checkSelfPermission(this,READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO), REQUEST_CODE_STORAGE_PERMISSION
                )
            } else {
                selectImage()
            }

//            when {
//                ContextCompat.checkSelfPermission(
//                    this,
//                    READ_MEDIA_IMAGES
//                ) == PackageManager.PERMISSION_GRANTED -> {
//                    // You can use the API that requires the permission.
//                    Toast.makeText(this, "Read media images permission granted", Toast.LENGTH_SHORT).show()
//                    selectImage()
//                }
//                ActivityCompat.shouldShowRequestPermissionRationale(
//                    this, READ_MEDIA_IMAGES) -> {
//                    // In an educational UI, explain to the user why your app requires this
//                    // permission for a specific feature to behave as expected, and what
//                    // features are disabled if it's declined. In this UI, include a
//                    // "cancel" or "no thanks" button that lets the user continue
//                    // using your app without granting the permission.
//                    AlertDialog.Builder(this)
//                        .setTitle("Storage Permission")
//                        .setMessage("Storage permission is needed in order to show images and videos")
//                        .setNegativeButton("Cancel"){dialog,_->
//                            Toast.makeText(this, "Read media storage permission denied!", Toast.LENGTH_SHORT).show()
//                            dialog.dismiss()
//                        }
//                        .setPositiveButton("OK"){_,_->
//                            requestPermissionLauncher.launch(READ_MEDIA_IMAGES)
//                        }
//                        .show()
//                }
//                else -> {
//                    // You can directly ask for the permission.
//                    // The registered ActivityResultCallback gets the result of this request.
//                    requestPermissionLauncher.launch(
//                        READ_MEDIA_IMAGES)
//                }
//            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//                val notGrantedPermissions=permissions.filterNot { permission->
//                    Log.i(TAG, "not granted permission: ${permission}")
//                    ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED
//                }
//                if (notGrantedPermissions.isNotEmpty()){
//                    val showRationale=notGrantedPermissions.any { permission->
//                        Log.i(TAG, "rationale permission: ${permission}")
//
//                        shouldShowRequestPermissionRationale(permission)
//                    }
//                    if (showRationale){
//                        AlertDialog.Builder(this)
//                            .setTitle("Storage Permission")
//                            .setMessage("Storage permission is needed in order to show images and videos")
//                            .setNegativeButton("Cancel"){dialog,_->
//                                Toast.makeText(this, "Read media storage permission denied!", Toast.LENGTH_SHORT).show()
//                                dialog.dismiss()
//                            }
//                            .setPositiveButton("OK"){_,_->
//                                videoImagesPermission.launch(notGrantedPermissions.toTypedArray())
//                            }
//                            .show()
//                    }else{
//                        videoImagesPermission.launch(notGrantedPermissions.toTypedArray())
//                    }
//                }else{
//                    Toast.makeText(this, "Read media storage permission granted", Toast.LENGTH_SHORT).show()
//                    selectImage()
//                }
//            }else{
//                if (ContextCompat.checkSelfPermission(this,readExternal) == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_SHORT).show()
//                    selectImage()
//                }else{
//                    if (shouldShowRequestPermissionRationale(readExternal)){
//                        AlertDialog.Builder(this)
//                            .setTitle("Storage Permission")
//                            .setMessage("Storage permission is needed in order to show images and video")
//                            .setNegativeButton("Cancel"){dialog,_->
//                                Toast.makeText(this, "Read external storage permission denied!", Toast.LENGTH_SHORT).show()
//                                dialog.dismiss()
//                            }
//                            .setPositiveButton("OK"){_,_->
//                                readExternalPermission.launch(readExternal)
//                            }
//                            .show()
//                    }else{
//                        readExternalPermission.launch(readExternal)
//                    }
//                }
//            }

//            val requestPermissionLauncher = registerForActivityResult(
//                ActivityResultContracts.RequestMultiplePermissions()
//            ) { permissions ->
//                if (permissions.getOrDefault(android.Manifest.permission.READ_MEDIA_IMAGES, false)) {
//                    // Permission granted
//                } else {
//                    // Handle permission denial
//                }
//            }
//
//            if (ContextCompat.checkSelfPermission(
//                    applicationContext,
//                    android.Manifest.permission.READ_MEDIA_IMAGES
//                )
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this@NoteDetailActivity,
//                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
//                    REQUEST_CODE_STORAGE_PERMISSION
//                )
//                requestPermissionLauncher.launch(
//                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
//                )
//            } else {
//                selectImage()
//            }


//            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
//                == PackageManager.PERMISSION_GRANTED) {
////                showDialog("Permission granted")
//                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
//                selectImage()
//            } else {
//                requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
//                    REQUEST_CODE_STORAGE_PERMISSION)
//            }

        }

        binding.misc.layoutAddUrl.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showAddURLDialog()
        }

        binding.misc.layoutDeleteNote.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showDeleteNoteDialog()
        }


    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//            when (requestCode) {
//                100 -> {
//                    Toast.makeText(this, "permissions granted", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } else {
//
//        }
//    }


    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                Toast.makeText(this, "Media permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Toast.makeText(this, "Media permissions not granted!", Toast.LENGTH_SHORT).show()
            }
        }

//    private val videoImagesPermission=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissionMap->
//        if (permissionMap.all { it.value }){
//            Toast.makeText(this, "Media permissions granted", Toast.LENGTH_SHORT).show()
//        }else{
//            Toast.makeText(this, "Media permissions not granted!", Toast.LENGTH_SHORT).show()
//        }
//    }
//    private val readExternalPermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
//        if (isGranted){
//            Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_SHORT).show()
//        }else{
//            Toast.makeText(this, "Read external storage permission denied!", Toast.LENGTH_SHORT).show()
//        }
//    }

//    private fun showDialog(msg: String) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Dialog")
//            .setMessage(msg)
//        val dialog = builder.create()
//        dialog.show()
//    }

    private fun showDeleteNoteDialog() {
        val builder = AlertDialog.Builder(this@NoteDetailActivity)
        val view: View = LayoutInflater.from(this).inflate(
            R.layout.layout_delete_note,
            findViewById(R.id.layout_DeleteNoteContainer)
        )
        builder.setView(view)
        val dialogDeleteNote: AlertDialog = builder.create()
        if (dialogDeleteNote.window != null) {
            dialogDeleteNote.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        view.findViewById<TextView>(R.id.tv_DeleteNote).setOnClickListener {
            viewModel.handleEvent(
                NoteDetailEvent.OnDeleteClick
            )
            dialogDeleteNote.dismiss()
        }
        view.findViewById<TextView>(R.id.tv_DeleteNote_Cancel).setOnClickListener {
            dialogDeleteNote.dismiss()
        }
        dialogDeleteNote.show()
    }

    private fun showAddURLDialog() {
        val builder = AlertDialog.Builder(this@NoteDetailActivity)
        val view: View = LayoutInflater.from(this).inflate(
            R.layout.layout_add_url,
            findViewById(R.id.layout_addUrlContainer)
        )
        builder.setView(view)
        val dialogAddURL: AlertDialog = builder.create()
        if (dialogAddURL.window != null) {
            dialogAddURL.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        val inputURL = view.findViewById<EditText>(R.id.et_url)
        inputURL.requestFocus()

        view.findViewById<TextView>(R.id.tv_AddUrl).setOnClickListener {
            if (inputURL.text.toString().trim().isEmpty()){
                showErrorState("Enter URL")
            } else if (!Patterns.WEB_URL.matcher(inputURL.text.toString()).matches()){
                showErrorState("Enter valid URL")
            } else {
                viewModel.handleEvent(
                    NoteDetailEvent.OnWebLinkChange(inputURL.text.toString().trim())
                )
                dialogAddURL.dismiss()
            }
        }

        view.findViewById<TextView>(R.id.tv_AddUrl_Cancel).setOnClickListener {
            dialogAddURL.dismiss()
        }

        dialogAddURL.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var fileName = ""

        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (data != null) {
                val selectedImageUri = data.data
                // 이건 official document에서 보장하는 내용
                selectedImageUri.let { returnUri ->
                    returnUri?.let { contentResolver.query(it, null, null, null, null) }
                }?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    cursor.moveToFirst()
//                    Log.i(TAG, "${cursor.getString(nameIndex)}")
//                    Log.i(TAG, "${cursor.getString(sizeIndex)}")
                    fileName = cursor.getString(nameIndex)
                }

//                Log.i(TAG, "onActivityResult: $selectedImageUri")
                if (selectedImageUri != null) {
                    Log.i(TAG, "onActivityResult: ${selectedImageUri!!.path!!.substring(selectedImageUri!!.path!!.lastIndexOf('/') + 1)}")

                    try {
                        // private storage issue 때문에 이미지 복사
                        FileUtils.application = application
                        FileUtils.cRes = contentResolver

                        val inputStream = FileUtils.getInputStream(selectedImageUri)
                        val path = this.getExternalFilesDir(null)
                        val folder = File(path, "images")
                        folder.mkdirs()
                        val outputFile = File(folder, fileName)
                        FileUtils.copyStreamToFile(inputStream!!, outputFile)

                        binding.ivNote.setImageURI(Uri.fromFile(outputFile))
                        binding.ivNote.visibility = View.VISIBLE
                        binding.ivDeleteImage.visibility = View.VISIBLE

                        binding.ivNote.tag = outputFile.path
//                        selectedImagePath = outputFile.path

                        viewModel.handleEvent(
                            NoteDetailEvent.OnNoteImageChange(outputFile.path)
                        )

//                        showImage(selectedImagePath!!)
//
//                        viewModel.handleEvent(
//                            NoteDetailEvent.OnNoteImageChange(getPathFromUri(selectedImageUri))
//                        )
//
//                        selectedImagePath = getPathFromUri(selectedImageUri)
                    } catch (e: Exception) {
                        showErrorState(e.toString())
                    }
                }
            }
        }
    }

//    private fun getPathFromUri(contentUri: Uri): String {
//        // contentResolver와 cursor에 대해 공부 필요!
//        val filePath: String
//        val cursor = contentResolver.query(contentUri, null, null, null, null)
//        if (cursor == null) {
//            filePath = contentUri.path.toString()
//        } else {
//            cursor.moveToFirst()
//            val index = cursor.getColumnIndex("_data")
//            filePath = cursor.getString(index)
//            cursor.close()
//        }
//        return filePath
//    }

    private fun selectImage() {
        // private storage issue 때문에 이미지 복사
        Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
            it.type="image/*"
            startActivityForResult(it, REQUEST_CODE_SELECT_IMAGE)
        }

//        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
//            it.type="image/*"
//            startActivityForResult(it, REQUEST_CODE_SELECT_IMAGE)
//        }

//        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
//            it.type = "image/*"
//            startActivityForResult(it, REQUEST_CODE_SELECT_IMAGE) }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_CODE_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission granted?")
//                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
                    selectImage()
                } else {
                    Log.d(TAG, "User declined and i can't ask")
                    showDialogToGetPermission()
                }
            }
        }

//        when(requestCode){
//            REQUEST_CODE_STORAGE_PERMISSION -> {
//                if (grantResults.isEmpty()) {
//                    throw RuntimeException("Empty permission result")
//                }
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "Permission granted?")
////                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
//                    selectImage()
//                } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)) {
//                    Log.d(TAG, "User declined, but i can still ask for more")
//
//                    requestPermissions(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO),
//                    REQUEST_CODE_STORAGE_PERMISSION)
//                } else {
//                    Log.d(TAG, "User declined and i can't ask")
//                    showDialogToGetPermission()
//                }
//            }
//        }
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissions request")
            .setMessage("We need the permission for some reason." +
            "You need to move on Settings to grant some permissions")
        builder.setPositiveButton("OK") { dialogInterface, i ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        builder.setNegativeButton("Later") { dialogInterface, i ->
        }
        val dialog = builder.create()
        dialog.show()

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

    private fun showURL(url: String){
        binding.tvWebUrl.text = url
        binding.layoutWebUrl.visibility = View.VISIBLE
        binding.ivDeleteWebUrl.visibility = View.VISIBLE
        binding.ivDeleteWebUrl.setOnClickListener {
            viewModel.handleEvent(
                NoteDetailEvent.OnNoteURLDeleteClick
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
                binding.etNoteTitle.text = note.title.toEditable()
                binding.tvDateTime.text = note.dateTime
                binding.etNoteSubtitle.text = note.subtitle.toEditable()
                binding.etNoteContent.text = note.noteContents.toEditable()

                if (!note.color.isNullOrEmpty()){
                    when(note.color){
                        ColorPINK->      binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color2).performClick()
                        ColorDARKBLUE->  binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color3).performClick()
                        ColorYELLOW->    binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color4).performClick()
                        ColorLIGHTBLUE-> binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color5).performClick()
                        else ->     binding.misc.layoutMisc.findViewById<ImageView>(R.id.iv_color1).performClick()
                    }
                    setSubtitleIndicatorColor(note.color)
//                    Log.i(TAG, "viewModel.note.observe color is ${note.color}")
                } else {
                    setSubtitleIndicatorColor(ColorBLACK) // 이렇게 하면... 항상 changeNoteColor()가 호출되지 않을까?
//                    Log.i(TAG, "viewModel.note.observe color is null")
                }

                if(!note.imagePath.isNullOrEmpty()) {
//                    selectedImagePath= note.imagePath.toString() // 이렇게 해도 되는 건가요????????
                    showImage(note.imagePath)
                }

                if (!note.webLink.isNullOrEmpty()) {
                    showURL(note.webLink)
                }

                if (note.noteId != "0") {
                    binding.misc.layoutDeleteNote.visibility = View.VISIBLE
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

        viewModel.noteColor.observe(
            this,
            Observer { noteColor ->
//                Log.i(TAG, "4 $selectedImagePath")
                setSubtitleIndicatorColor(noteColor)
                Log.i(TAG, "viewModel.noteColor.observe")
            }
        )

        viewModel.noteImage.observe(
            this,
            Observer{ imagePath ->
                if (!imagePath.isNullOrEmpty()) {
//                    selectedImagePath = imagePath // 얘를 이렇게 저장하면 안되는거?
//                Log.i(TAG, "3 $selectedImagePath")
                    showImage(imagePath)
                } else {
                    binding.ivNote.visibility = View.GONE
                    binding.ivDeleteImage.visibility = View.GONE
                }
                Log.i(TAG, "viewModel.noteImage.observe")
            }
        )

        viewModel.webLink.observe(
            this,
            Observer { webLink ->
                showURL(webLink)
                Log.i(TAG, "viewModel.webLink.observe")
            }
        )

        viewModel.deleted.observe(
            this,
            Observer {
                finish()
                Log.i(TAG, "viewModel.deleted.observe")
            }
        )


        viewModel.noteImageDeleted.observe(
            this,
            Observer {
                binding.ivNote.visibility = View.GONE
                binding.ivDeleteImage.visibility = View.GONE
                Log.i(TAG, "viewModel.noteImageDeleted.observe")
            }
        )

        viewModel.noteURLDeleted.observe(
            this,
            Observer {
                binding.tvWebUrl.text = ""
                binding.tvWebUrl.visibility = View.GONE
                binding.layoutWebUrl.visibility = View.GONE
                Log.i(TAG, "viewModel.noteURLDeleted.observe")
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