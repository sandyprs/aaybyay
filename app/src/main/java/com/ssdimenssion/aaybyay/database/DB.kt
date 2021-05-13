package com.ssdimenssion.aaybyay.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Txn::class],version = 1,exportSchema = false)
abstract class DB :RoomDatabase(){
    abstract fun txnDao(): TxnDao
}