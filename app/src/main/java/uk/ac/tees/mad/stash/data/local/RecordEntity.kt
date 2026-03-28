package uk.ac.tees.mad.stash.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey
    val recordID: String,
    val title: String,
    val value: String
)
