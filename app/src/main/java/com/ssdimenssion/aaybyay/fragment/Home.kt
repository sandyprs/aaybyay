package com.ssdimenssion.aaybyay.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.ssdimenssion.aaybyay.R
import com.ssdimenssion.aaybyay.database.DB
import com.ssdimenssion.aaybyay.database.Txn
import com.ssdimenssion.aaybyay.utils.DBAsyncTask
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Home : Fragment() {

    lateinit var date:EditText
    lateinit var purpose:EditText
    lateinit var amt:EditText
    lateinit var add:Button
    lateinit var drop:ImageView
    lateinit var db: DB


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        date = view.findViewById(R.id.date)
        purpose = view.findViewById(R.id.purpose)
        amt = view.findViewById(R.id.amt)
        add = view.findViewById(R.id.add)
        drop = view.findViewById(R.id.drop)

        db = activity?.let { DB.geInstance(it.applicationContext) }!!


        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date()).toString()
        date.setText(currentDate)

        drop.setOnClickListener {
            purpose.setText("Extra Expenses")
        }

        date.setOnClickListener {
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
                        date.setText(formatday.toString() + "-" + formatMon.toString() + "-" + year.toString())

                    },
                    year,
                    mon,
                    day
                )

            }?.show()


        }

        add.setOnClickListener {
            if(date.text.isNotEmpty() && purpose.text.isNotEmpty() && amt.text.isNotEmpty()){
                val txn = Txn(
                    0,
                    date.text.toString(),
                    purpose.text.toString(),
                    amt.text.toString().toDouble()
                )
                //insertInDB(txn)

                if(DBAsyncTask.geInstance(db=db,data=txn,mode=1).execute().get().isEmpty()){
                    Toast.makeText(activity,"one record added",Toast.LENGTH_SHORT).show()
                }

                purpose.text.clear()
                amt.text.clear()

            }else{
                Toast.makeText(activity,"Please fill all fields",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

//    private fun insertInDB(txn: Txn) {
//        val thread = Thread {
//            db.txnDao().insertAll(txn)
//            activity?.runOnUiThread{
//                Toast.makeText(activity,"new record added",Toast.LENGTH_LONG).show()
//            }
//
//        }
//        thread.start()
//
//    }

//    class DBAsyncTask(val db:DB,val data:Txn, val mode:Int):AsyncTask<Void,Void,Boolean>(){
//        //val db= Room.databaseBuilder(context, DB::class.java,"txn-db").build()
//        override fun doInBackground(vararg params: Void?): Boolean {
//            return if (mode == 1){
//                db.txnDao().insertAll(data)
//                db.close()
//                true
//            }else{
//                db.txnDao().delete(data)
//                db.close()
//                true
//            }
//        }
//
//    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

}