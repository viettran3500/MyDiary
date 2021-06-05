package com.viet.mydiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.viet.mydiary.fragment.calendarfragment.CalendarFragment
import com.viet.mydiary.fragment.DiaryFragment
import com.viet.mydiary.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val FRAGMENT_DIARY = 1
    val FRAGMENT_CALENDAR = 2
    val FRAGMENT_SETTING = 3

    var currentFragment = FRAGMENT_DIARY

    var diaryFragment = DiaryFragment(null)
    var calendarFragment =
        CalendarFragment()
    var settingFragment = SettingFragment()

//    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
//    val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
//    val yearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigation.setNavigationItemSelectedListener(this)

        replaceFragment(diaryFragment,"1")

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if(supportFragmentManager.backStackEntryCount == 1)
                finish()
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_diary -> {
                if (FRAGMENT_DIARY != currentFragment) {
                    replaceFragment(diaryFragment,"1")
                    currentFragment = FRAGMENT_DIARY
                }
            }
            R.id.nav_calendar -> {
                if (FRAGMENT_CALENDAR != currentFragment) {
                    replaceFragment(calendarFragment,"2")
                    currentFragment = FRAGMENT_CALENDAR
                }
            }
            R.id.nav_setting -> {
                if (FRAGMENT_SETTING != currentFragment) {
                    replaceFragment(settingFragment,"3")
                    currentFragment = FRAGMENT_SETTING
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceFragment(fragment: Fragment, s: String) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(s)
        fragmentTransaction.commit()

        when(fragment){
            diaryFragment->changeTitle("My Diary")
            calendarFragment->changeTitle("Calendar")
            settingFragment->changeTitle("Setting")
        }
    }

    fun changeTitle(title: String){
        toolbar.title = title
    }


}