package com.example.fran.canchas2

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.fran.canchas2.views.MainFragment
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.fran.canchas2.views.MapsActivity


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val MAIN_VIEWS_PACKAGE = "com.example.fran.canchas2.views."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)

        val fab = fab as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = drawer_layout as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = nav_view as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = drawer_layout as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {
            //val fragment = Class.forName(MAIN_VIEWS_PACKAGE + "MainFragment")
            showFragment(MainFragment())

        } else if (id == R.id.nav_reservations) {

        } else if (id == R.id.nav_tournaments) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_map) {
            showFragment(MapsActivity())
        }

        val drawer = drawer_layout as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun showFragment(fragment: Fragment){
        val fm = supportFragmentManager
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
    fun showFragment(activity: FragmentActivity){
        val intent = Intent(this, activity.javaClass)
        startActivity(intent)
    }
}
