package com.example.fran.canchas2.views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fran.canchas2.R.layout.content_main

/**
 * Created by fran on 08/10/17.
 */
class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(content_main,container,false)
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

}