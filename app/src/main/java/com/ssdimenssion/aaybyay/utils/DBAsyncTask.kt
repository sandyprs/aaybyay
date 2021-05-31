package com.ssdimenssion.aaybyay.utils

import android.os.AsyncTask
import com.ssdimenssion.aaybyay.database.DB
import com.ssdimenssion.aaybyay.database.Txn

class DBAsyncTask private constructor(val db:DB, val data: Txn?, val mode:Int, var from: String, var to: String): AsyncTask<Void, Void, List<Txn>>(){

    companion object{

        fun geInstance(
            db: DB,
            data: Txn? = null,
            mode: Int,
            from: String = "",
            to: String = ""
        ): DBAsyncTask {
            return DBAsyncTask(db, data, mode, from, to)
        }
    }

    override fun doInBackground(vararg params: Void?): List<Txn> {
        when (mode) {
            1 -> {
                if (data != null) {
                    db.txnDao().insertAll(data)
                }
                return emptyList()
            }
            2 -> {
                if (data != null) {
                    db.txnDao().delete(data)
                }
                return emptyList()
            }
            else -> {
                return if (from.isNotEmpty() || to.isNotEmpty()) {
                    if (from.isNotEmpty() && to.isEmpty()){
                        to = from
                    }else if (from.isEmpty() && to.isNotEmpty()){
                        from = to
                    }
                    db.txnDao().getFiltered(from, to)
                } else {
                    db.txnDao().getAll()

                }
            }
        }
    }
}