package com.shinjaehun.winternotes.model

data class Note (
    var noteId: String,
    val title: String,
    val dateTime: String,
    val subtitle: String,
    val noteContents: String,
//    val imagePath: String?,
    val color: String?,
//    val webLink: String?
)