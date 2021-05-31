package com.ssdimenssion.aaybyay.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Txn::class],version = 1,exportSchema = false)
abstract class DB :RoomDatabase(){
    abstract fun txnDao(): TxnDao

    companion object{
        @Volatile
        private var INSTANCE: DB? = null

        fun geInstance(context: Context): DB = INSTANCE ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                DB ::class.java,
                "txn-db"
            ).build()
            INSTANCE = instance
            return@synchronized instance
        }
    }
}