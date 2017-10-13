package com.example.fran.canchas2.views

import android.app.DatePickerDialog
import android.app.ListFragment
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.fran.canchas2.R
import com.example.fran.canchas2.utils.NetworkUtils
import kotlinx.android.synthetic.main.content_reservations.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.URL
import java.util.*
import android.widget.ArrayAdapter
import com.example.fran.canchas2.R.id.fieldNameView
import com.example.fran.canchas2.R.id.reservationHour
import com.example.fran.canchas2.R.layout.reservation_item
import com.example.fran.canchas2.utils.Reservation
import com.example.fran.canchas2.utils.ReservationsAdapter


/**
 * Created by fran on 10/10/17.
 */
class ReservationsFragment : Fragment(){
    internal val CLUB_ID = "59b0ac5ab729bb0042bfcc8b"
    internal val API_URL = "https://canchas.ml/clubes/"
    var response: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.content_reservations,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pickDate.setOnClickListener { showDatePickerDialog(it) }
        reservationsRequest.setOnClickListener {  performSearchTask() }
    }

    fun showDatePickerDialog(view: View){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    datePicked.text = "$dayOfMonth/$monthOfYear/$year"
                }, year, month, day)
        dialog.show()

    }

    fun performSearchTask(){
        val searchTask = SearchTask()
        reservationsList.adapter = null
        searchTask.execute(URL(API_URL + CLUB_ID))
        responseProgressBar.visibility = View.VISIBLE

    }

    inner class SearchTask : AsyncTask<URL, Void, String>() {

        // COMPLETED (2) Override the doInBackground metehod to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        override fun doInBackground(vararg params: URL): String? {
            var searchResults: String? = null
            try {
                responseProgressBar.progress = 25
                searchResults = NetworkUtils().responseFromHttpUrl(params[0])
                responseProgressBar.progress = 50

            } catch (e: IOException) {
                e.printStackTrace()
            }

            response = searchResults

            return searchResults
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        override fun onPostExecute(searchResults: String?) {
            if (searchResults != null && searchResults != "") {

                val reservations = JSONArray(searchResults)
                var reservationsArray = ArrayList<Reservation>()
                responseProgressBar.visibility = View.GONE


                for (i in 0 until reservations.length()) {
                    var JSONreservation = reservations.getJSONObject(i)
                    var fieldName = JSONreservation.getString("nombre")
                    var reservas = JSONreservation.getJSONArray("reservas")
                    for (j in 0 until reservas.length()) {

                        var begginingHour = Date(reservas.getJSONObject(j).getString("desde").toLong()).hours.toString()
                        var endHour = Date(reservas.getJSONObject(j).getString("hasta").toLong()).hours.toString()
                        var reservation = Reservation(fieldName,begginingHour,endHour,"Franco")
                        reservationsArray.add(reservation)
                    }
                }

                val adapter = ReservationsAdapter(context,reservationsArray.filterDateResults(null))
                reservationsList.adapter = adapter
            }
        }

        fun ArrayList<Reservation>.filterDateResults(filterDate: String?): ArrayList<Reservation> {
            if(filterDate == null){
                return this
            }

            var list = ArrayList<Reservation>()

            for (j in 0 until this.size) {

                if(compareDateDay(Date(this[j].reservationBegginingHour),Date(filterDate))){
                    list.add(this[j])
                }
            }
            return list
        }

        fun compareDateDay(date1: Date,date2: Date):Boolean =
                ((date1.year)==(date2.year) && (date1.month)==(date2.month) && (date1.day)==(date2.day))
    }

}