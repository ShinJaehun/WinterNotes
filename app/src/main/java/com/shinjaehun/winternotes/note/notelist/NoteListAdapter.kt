package com.shinjaehun.winternotes.note.notelist

import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shinjaehun.winternotes.R
import com.shinjaehun.winternotes.common.ColorBLACK
import com.shinjaehun.winternotes.common.simpleDate
import com.shinjaehun.winternotes.databinding.ItemContainerNoteBinding
import com.shinjaehun.winternotes.model.Note

class NoteListAdapter(
    val event: MutableLiveData<NoteListEvent> = MutableLiveData() // 이게 필요한 이유는 click listener 때문
): RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class NoteViewHolder(val binding: ItemContainerNoteBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(
            ItemContainerNoteBinding.inflate(inflater, parent, false)
        )
    }

    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = allNotes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder){
            with(allNotes[position]){
                binding.tvTitle.text = this.title

                if (this.subtitle.trim().isEmpty()){
                    binding.tvSubtitle.visibility= View.GONE
                } else {
                    binding.tvSubtitle.text = this.subtitle
                }

                binding.tvDateTime.text = simpleDate(this.dateTime)

                if (!this.imagePath.isNullOrEmpty()) {
                    binding.rivImagePreview.setImageURI(Uri.parse(this.imagePath))
                    binding.rivImagePreview.visibility = View.VISIBLE
                } else {
                    binding.rivImagePreview.visibility = View.GONE
                }

                val gradientDrawable = binding.layoutNote.background as GradientDrawable
                if (!this.color.isNullOrEmpty()) {
                    gradientDrawable.setColor(Color.parseColor(this.color))
                } else {
                    gradientDrawable.setColor(Color.parseColor(ColorBLACK))
                }

                binding.layoutNote.setOnClickListener {
                    event.value = NoteListEvent.OnNoteItemClick(position)
                }
            }
        }
    }
}