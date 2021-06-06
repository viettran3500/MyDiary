package com.viet.mydiary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.viet.mydiary.R
import com.viet.mydiary.fragment.calendarfragment.CalendarFragment
import com.viet.mydiary.fragment.DiaryFragment
import com.viet.mydiary.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val FRAGMENT_DIARY = 1
    val FRAGMENT_CALENDAR = 2
    val FRAGMENT_SETTING = 3
    val FRAGMENT_DIARY_CALENDAR = 4
    var currentFragment = FRAGMENT_DIARY

    var diaryFragment = DiaryFragment(null)
    var calendarFragment =
        CalendarFragment()
    var settingFragment = SettingFragment()

    var stack = Stack<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigation.setNavigationItemSelectedListener(this)
        navigation.setCheckedItem(R.id.nav_diary)
        replaceFragment(diaryFragment)
        stack.push("1")

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (currentFragment == FRAGMENT_DIARY_CALENDAR) {
                navigation.setCheckedItem(R.id.nav_calendar)
                replaceFragment(calendarFragment)
                currentFragment = FRAGMENT_CALENDAR
            } else if (stack.pop() != "1") {
                when (stack.peek()) {
                    "3" -> {
                        navigation.setCheckedItem(R.id.nav_setting)
                        replaceFragment(settingFragment)
                        currentFragment = FRAGMENT_SETTING
                    }
                    "2" -> {
                        navigation.setCheckedItem(R.id.nav_calendar)
                        replaceFragment(calendarFragment)
                        currentFragment = FRAGMENT_CALENDAR
                    }
                    "1" -> {
                        navigation.setCheckedItem(R.id.nav_diary)
                        replaceFragment(diaryFragment)
                        currentFragment = FRAGMENT_DIARY
                    }
                }
            } else
                super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_diary -> {
                if (FRAGMENT_DIARY != currentFragment) {
                    replaceFragment(diaryFragment)
                    currentFragment = FRAGMENT_DIARY
                    stack.push("1")
                }
            }
            R.id.nav_calendar -> {
                if (FRAGMENT_CALENDAR != currentFragment) {
                    replaceFragment(calendarFragment)
                    currentFragment = FRAGMENT_CALENDAR
                    stack.push("2")
                }
            }
            R.id.nav_setting -> {
                if (FRAGMENT_SETTING != currentFragment) {
                    replaceFragment(settingFragment)
                    currentFragment = FRAGMENT_SETTING
                    stack.push("3")
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

        when (fragment) {
            diaryFragment -> changeTitle("My Diary")
            calendarFragment -> changeTitle("Calendar")
            settingFragment -> changeTitle("Setting")
        }
    }

    fun changeTitle(title: String) {
        toolbar.title = title
    }
}