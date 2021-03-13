package com.robusta.photoweather.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "history")
data class History(
    @PrimaryKey @ColumnInfo(name = "id") val picId: String,
    val locationName: String,
    val thumbnail: String, // Base64 thumbnail
    val pic: String // Path of the image
) {
    override fun toString() = picId
}
