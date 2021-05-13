package com.rpaysolution.aaybyay.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.room.Room
import com.rpaysolution.aaybyay.R
import com.rpaysolution.aaybyay.database.DB
import com.rpaysolution.aaybyay.database.Txn
import com.rpaysolution.aaybyay.model.SingleTxn
import java.util.*


class Home : Fragment() {

    lateinit var date:EditText
    lateinit var purpose:EditText
    lateinit var amt:EditText
    lateinit var add:Button
    lateinit var drop:ImageView

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

                DBAsyncTask(context!!,txn,1).execute().get()
                Toast.makeText(activity,"Record added",Toast.LENGTH_SHORT).show()
                date.text.clear()
                purpose.text.clear()
                amt.text.clear()

            }else{
                Toast.makeText(activity,"Please fill all fields",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    class DBAsyncTask(val context: Context,val data:Txn, val mode:Int):AsyncTask<Void,Void,Boolean>(){
        val db= Room.databaseBuilder(context, DB::class.java,"txn-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            if (mode == 1){
                db.txnDao().insertAll(data)
                db.close()
                return true
            }else{
                db.txnDao().delete(data)
                db.close()
                return true
            }
        }

    }

}