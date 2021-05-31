package com.ssdimenssion.aaybyay.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssdimenssion.aaybyay.R
import com.ssdimenssion.aaybyay.adapter.HistoryAdapter
import com.ssdimenssion.aaybyay.database.DB
import com.ssdimenssion.aaybyay.database.Txn
import com.ssdimenssion.aaybyay.utils.DBAsyncTask
import java.util.*
import kotlin.collections.ArrayList


class History : Fragment(), HistoryAdapter.OnDeleteItem {
    lateinit var hist: RecyclerView
    lateinit var adapter: HistoryAdapter
    lateinit var layoutManager: LinearLayoutManager
    var txns = arrayListOf<Txn>()
    var newTxns = listOf<Txn>()
    lateinit var dateFrom: EditText
    lateinit var dateTo: EditText
    lateinit var search: Button
    lateinit var searchAll: Button
    lateinit var total: TextView
    lateinit var txt: TextView
    lateinit var db: DB


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        hist = view.findViewById(R.id.hist)
        dateFrom = view.findViewById(R.id.dateFrom)
        dateTo = view.findViewById(R.id.dateTo)
        search = view.findViewById(R.id.search)
        total = view.findViewById(R.id.total)
        txt = view.findViewById(R.id.txt)
        searchAll = view.findViewById(R.id.searchAll)
        adapter = HistoryAdapter(context!!, this, txns)
        db = activity?.let { DB.geInstance(it.applicationContext) }!!
        hist.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        hist.layoutManager = layoutManager
        getFilteredData()

        view.findViewById<ImageView>(R.id.fromClear).setOnClickListener {
            dateFrom.text.clear()
        }

        view.findViewById<ImageView>(R.id.toClear).setOnClickListener {
            dateTo.text.clear()
        }

        searchAll.setOnClickListener {
            searchAll.visibility = View.GONE
            dateFrom.text.clear()
            dateTo.text.clear()
            //txns.clear()
            getFilteredData()
        }

        dateFrom.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val mon = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { view, year, monthOfYear, dayOfMonth ->
                        var formatMon: String = "0"
                        var formatday: String = "0"
                        val month = monthOfYear + 1
                        formatMon = if (month < 10) {
                            "0${month.toString()}"
                        } else {
                            month.toString()
                        }
                        formatday = if (dayOfMonth < 10) {
                            "0${dayOfMonth.toString()}"
                        } else {
                            dayOfMonth.toString()
                        }
                        dateFrom.setText(formatday.toString() + "-" + formatMon.toString() + "-" + year.toString())

                    },
                    year,
                    mon,
                    day
                )

            }?.show()


        }

        dateTo.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val mon = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { view, year, monthOfYear, dayOfMonth ->
                        var formatMon: String = "0"
                        var formatday: String = "0"
                        val month = monthOfYear + 1
                        formatMon = if (month < 10) {
                            "0${month.toString()}"
                        } else {
                            month.toString()
                        }
                        formatday = if (dayOfMonth < 10) {
                            "0${dayOfMonth.toString()}"
                        } else {
                            dayOfMonth.toString()
                        }
                        dateTo.setText(formatday.toString() + "-" + formatMon.toString() + "-" + year.toString())

                    },
                    year,
                    mon,
                    day
                )

            }?.show()


        }

        search.setOnClickListener {
            if (dateFrom.text.isNotEmpty() || dateTo.text.isNotEmpty()) {
                getFilteredData()
                searchAll.visibility = View.VISIBLE
                val inputManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(it.windowToken, 0)
            } else {
                Toast.makeText(
                    activity,
                    "Before filter select Date From or Date To",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        return view
    }

    fun setTotalSpend() {
        var sum = 0.0
        for (t in txns) {
            sum += t.Amount
        }

        total.text = "Total spend :- ₹ $sum"

    }

    fun getFilteredData() {
        txns.clear()
        newTxns = DBAsyncTask.geInstance(db=db,from=dateFrom.text.toString(),
            to=dateTo.text.toString(),mode = 3).execute().get()
        txns.addAll(newTxns)
        if(txns.isEmpty()){
            txt.visibility=View.VISIBLE
        }
        adapter.notifyDataSetChanged()
        setTotalSpend()

    }

    override fun deleteItem(data: Txn) {
        if(DBAsyncTask.geInstance(db=db,data=data,mode = 2).execute().get().isEmpty()) {
            Toast.makeText(context, "Record Deleted", Toast.LENGTH_SHORT).show()
            getFilteredData()
        }

    }

}