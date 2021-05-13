package com.ssdimenssion.aaybyay.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TxnDao {
    @Query("SELECT * FROM txns")
    fun getAll(): List<Txn>

    @Query("SELECT * FROM txns WHERE date BETWEEN :from and :to")
    fun getFiltered(from:String,to:String): List<Txn>

    @Query("SELECT * FROM txns WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Txn>

    @Insert
    fun insertAll(vararg users: Txn)

    @Delete
    fun delete(txn: Txn)
}