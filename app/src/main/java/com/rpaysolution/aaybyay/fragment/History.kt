package com.rpaysolution.aaybyay.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.rpaysolution.aaybyay.R
import com.rpaysolution.aaybyay.adapter.HistoryAdapter
import com.rpaysolution.aaybyay.database.DB
import com.rpaysolution.aaybyay.database.Txn
import java.util.*
import kotlin.collections.ArrayList


class History : Fragment(),HistoryAdapter.OnDeleteItem {
    lateinit var hist:RecyclerView
    lateinit var adapter:HistoryAdapter
    lateinit var layoutManager: LinearLayoutManager
    var txns = arrayListOf<Txn>()
    lateinit var dateFrom:EditText
    lateinit var dateTo:EditText
    lateinit var search:Button
    lateinit var searchAll:Button
    lateinit var total:TextView


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
        searchAll = view.findViewById(R.id.searchAll)
        adapter = HistoryAdapter(context!!,this, txns)
        var newTxns = DBAsyncTask(activity as Context,dateFrom.text.toString(),dateTo.text.toString()).execute().get() as ArrayList<Txn>
        txns.addAll(newTxns)
        setTotalSpend(newTxns)
        adapter = HistoryAdapter(context!!,this, txns)
        hist.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        hist.layoutManager = layoutManager

        searchAll.setOnClickListener {
            searchAll.visibility = View.GONE
            dateFrom.text.clear()
            dateTo.text.clear()
            txns.clear()
            newTxns = DBAsyncTask(activity as Context,dateFrom.text.toString(),dateTo.text.toString()).execute().get() as ArrayList<Txn>
            txns.addAll(newTxns)
            setTotalSpend(newTxns)
            adapter.notifyDataSetChanged()
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
                            var formatMon:String ="0"
                            var formatday:String ="0"
                            val month = monthOfYear + 1
                            formatMon = if(month < 10){
                                "0${month.toString()}"
                            }else{
                                month.toString()
                            }
                            formatday = if(dayOfMonth < 10){
                                "0${dayOfMonth.toString()}"
                            }else{
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
                            var formatMon:String ="0"
                            var formatday:String ="0"
                            val month = monthOfYear + 1
                            formatMon = if(month < 10){
                                "0${month.toString()}"
                            }else{
                                month.toString()
                            }
                            formatday = if(dayOfMonth < 10){
                                "0${dayOfMonth.toString()}"
                            }else{
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
            if (dateFrom.text.isNotEmpty() && dateTo.text.isNotEmpty()) {
                txns.clear()
                newTxns = DBAsyncTask(activity as Context, dateFrom.text.toString(), dateTo.text.toString()).execute().get() as ArrayList<Txn>
                txns.addAll(newTxns)
                setTotalSpend(newTxns)
                adapter.notifyDataSetChanged()
                searchAll.visibility = View.VISIBLE
                val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(it.windowToken, 0)
            }else{
                Toast.makeText(activity,"Before filter select Date From and Date To", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    class DBAsyncTask(val context: Context,val from:String,val to:String): AsyncTask<Void, Void, List<Txn>>(){
        val db= Room.databaseBuilder(context, DB::class.java,"txn-db").build()
        override fun doInBackground(vararg params: Void?): List<Txn> {
            return if (from.isNotEmpty() && to.isNotEmpty()) {
                val txns = db.txnDao().getFiltered(from,to)
                db.close()
                txns
            }else{
                val txns = db.txnDao().getAll()
                db.close()
                txns
            }
        }

    }

    override fun onDestroy() {

        super.onDestroy()
    }

    fun setTotalSpend(txns:ArrayList<Txn>){
        var sum = 0.0
        for (t in txns){
            sum+=t.Amount
        }

        total.text = "Total spend :- ₹ $sum"

    }

    override fun deleteItem(data: Txn) {
        if(Home.DBAsyncTask(context!!,data,2).execute().get()){
            Toast.makeText(context,"Record Deleted", Toast.LENGTH_SHORT ).show()
            txns.clear()
            val newtxns = DBAsyncTask(activity as Context,dateFrom.text.toString(),dateTo.text.toString()).execute().get() as ArrayList<Txn>
            txns.addAll(newtxns)
            setTotalSpend(newtxns)
//            println("db $txns")
            adapter.notifyDataSetChanged()

        }
    }

}