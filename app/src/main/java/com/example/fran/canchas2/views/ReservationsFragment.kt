package com.example.fran.canchas2.views

import android.app.DatePickerDialog
import android.app.ListFragment
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
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
        initializeProgressBar(responseProgressBar)
        reservationsRequest.setOnClickListener {  performSearchTask() }
    }

    fun initializeProgressBar(progressBar: ProgressBar){
        progressBar.progress = 0
        progressBar.max = 100
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
        searchTask.execute(URL(API_URL + CLUB_ID))
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

                try {
                    val reservations = JSONArray(searchResults)
                    responseProgressBar.progress = 100

                    for (i in 0 until reservations.length()) {
                        var reservation = reservations.getJSONObject(i)
                        var fieldName = reservation.getString("nombre")
                        var reservas = reservation.getJSONArray("reservas")
                        for (j in 0 until reservas.length()) {
                            var textToAppend = TextView(context)
                            textToAppend.text = fieldName + " - "

                            textToAppend.append(Date(reservas.getJSONObject(i).getString("desde").toLong()).hours.toString())
                            textToAppend.append(" hs")

                            reservationsList.addView(textToAppend)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                responseProgressBar.visibility = 4
            }
        }

        fun JSONArray.filterDateResults(filterDate: Date): List<Date> {
            var list = emptyList<Date>()
            for (j in 0 until this.length()) {
                if(compareDateDay(Date(this.getJSONObject(j).getString("desde").toLong()),filterDate)){
                    list += (Date(this.getJSONObject(j).getString("desde").toLong()))
                }
            }
            return list
        }

        fun compareDateDay(date1: Date,date2: Date):Boolean =
                ((date1.year)==(date2.year) && (date1.month)==(date2.month) && (date1.day)==(date2.day))
    }

}