package com.example.fran.canchas2.views

import android.app.DatePickerDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fran.canchas2.R
import com.example.fran.canchas2.utils.NetworkUtils
import kotlinx.android.synthetic.main.content_reservations.*
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.util.*
import com.example.fran.canchas2.utils.Reservation
import com.example.fran.canchas2.utils.ReservationsAdapter
import java.text.SimpleDateFormat
import android.widget.ArrayAdapter
import kotlin.collections.ArrayList


/**
 * Created by fran on 10/10/17.
 */
class ReservationsFragment : Fragment(){
    internal val CLUB_ID = "59b0ac5ab729bb0042bfcc8b"
    internal val API_URL = "https://canchas.ml/clubes/"
    var response: String? = null
    var dayPicked: String? = null
    var fieldsArray = ArrayList<String>()

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
                    datePicked.text = "$dayOfMonth-" + (monthOfYear+1) +"-$year"
                    dayPicked = "$dayOfMonth-" + (monthOfYear+1) +"-$year"
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

        override fun doInBackground(vararg params: URL): String? {
            if(response!= null){
                return response
            }

            var searchResults: String? = null
            try {
                searchResults = NetworkUtils().responseFromHttpUrl(params[0])
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

                val fmtDayOut = SimpleDateFormat("dd-MM-yyyy")
                val fmtIn = SimpleDateFormat("s")
                var filterDate: Date? = null
                if(dayPicked != null) {
                    filterDate = fmtDayOut.parse(dayPicked)
                }
                fieldsArray.add("All")
                for (i in 0 until reservations.length()) {
                    var JSONreservation = reservations.getJSONObject(i)
                    var fieldName = JSONreservation.getString("nombre")
                    var reservas = JSONreservation.getJSONArray("reservas")
                    fieldsArray.add(fieldName)
                    for (j in 0 until reservas.length()) {

                        var startDate = fmtIn.parse((reservas.getJSONObject(j).getString("desde").toLong()/1000).toString())
                        var endDate = fmtIn.parse(reservas.getJSONObject(j).getString("hasta"))

                        var reservation = Reservation(fieldName,startDate,endDate,"Franco")
                        reservationsArray.add(reservation)
                    }
                }

                val adapter = ReservationsAdapter(context,reservationsArray.filterDateResults(filterDate, ::isEqualDay))
                reservationsList.adapter = adapter
                showSpinner()
            }
        }

        fun ArrayList<Reservation>.filterDateResults(filterDate: Date?,compareDate: (Date,Date)->Boolean ): ArrayList<Reservation> {
            if(filterDate == null){
                return this
            }

            var list = ArrayList<Reservation>()

            for (j in 0 until this.size) {
                if(compareDate(this[j].reservationDate,filterDate)){
                    list.add(this[j])
                }
            }
            return list
        }
        
        fun ArrayList<Reservation>.filterFieldName(filterFieldName: String? ): ArrayList<Reservation> {
            if(filterFieldName == null){
                return this
            }

            var list = ArrayList<Reservation>()

            for (j in 0 until this.size) {
                if(this[j].fieldName == filterFieldName){
                    list.add(this[j])
                }
            }
            return list
        }
    }

    fun showSpinner(){

        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, fieldsArray)
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
// Apply the adapter to the spinner
        spinner.adapter = adapter
        spinner.visibility = View.VISIBLE
    }
}

fun isEqualHour(dateToCompare: Date,against: Date): Boolean{

    if(     dateToCompare.hours != against.hours ||
            dateToCompare.day != against.day ||
            dateToCompare.month != against.month ||
            dateToCompare.year != against.year){
        return false
    }
    return true
}
fun isEqualDay(dateToCompare: Date,against: Date): Boolean{
    if(     dateToCompare.day != against.day ||
            dateToCompare.month != against.month ||
            dateToCompare.year != against.year){
        return false
    }
    return true
}