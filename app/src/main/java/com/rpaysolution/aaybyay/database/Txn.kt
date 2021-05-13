package com.rpaysolution.aaybyay.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "txns")
data class Txn(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "date") val date:String,
    @ColumnInfo(name = "purpose") val purpose:String,
    @ColumnInfo(name = "Amount") val Amount:Double
)
