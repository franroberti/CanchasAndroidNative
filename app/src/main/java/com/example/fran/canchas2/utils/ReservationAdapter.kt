package com.example.fran.canchas2.utils

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.fran.canchas2.R
import com.example.fran.canchas2.R.layout.reservation_item
import java.text.SimpleDateFormat


class ReservationsAdapter(context: Context, reservations: ArrayList<Reservation>) : ArrayAdapter<Reservation>(context, 0, reservations) {

    override fun getView(position: Int, reservationView: View?, parent: ViewGroup): View {
        var convertView = reservationView

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(reservation_item, parent, false)
        }

        val reservationHour = convertView?.findViewById<TextView>(R.id.reservationHour)
        val reservationClient = convertView?.findViewById<TextView>(R.id.reservationClient)
        val fieldNameView =  convertView?.findViewById<TextView>(R.id.fieldNameView)

        // Populate the data into the template view using the data object
        val reservation = getItem(position)
        val fmtOut = SimpleDateFormat("dd-MM-yyyy hh:mm")

        reservationHour?.text = fmtOut.format(reservation.reservationDate)
        reservationClient?.text = reservation.reservationCurstomerName
        fieldNameView?.text = reservation.fieldName

        // Return the completed view to render on screen
        return convertView!!
        }
}