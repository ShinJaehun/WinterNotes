package com.shinjaehun.winternotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notes", indices = [Index("noteId")])
data class RoomNote (
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date_time")
    val dateTime: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    @ColumnInfo(name = "note_contents")
    val noteContents: String,

//    @ColumnInfo(name = "image_path")
//    val imagePath: String? = null,

    @ColumnInfo(name = "color")
    val color: String? = null,

//    @ColumnInfo(name = "web_link")
//    val webLink: String? = null
) : java.io.Serializable {

//    @PrimaryKey(autoGenerate = true)
//    var noteId: Int = 0

//    override fun toString(): String {
//        return "$title : $dateTime"
//    }
}