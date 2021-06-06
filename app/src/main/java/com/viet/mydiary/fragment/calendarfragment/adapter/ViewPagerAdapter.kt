package com.viet.mydiary.fragment.calendarfragment.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.viet.mydiary.fragment.calendarfragment.*
import com.viet.mydiary.utils.dayClick
import com.viet.mydiary.utils.lastDayClick
import com.viet.mydiary.utils.maxFragment
import com.viet.mydiary.utils.startFragment
import java.util.*
import kotlin.math.abs

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {

    var listFragment: MutableMap<Int, MonthFragment> = hashMapOf()

    override fun getCount(): Int {
        return maxFragment
    }

    override fun getItem(position: Int): Fragment {
        val fragmentMonth: MonthFragment? = listFragment[position]
        return if (fragmentMonth != null) {
            fragmentMonth
        } else {
            listFragment[position] = MonthFragment(position)
            MonthFragment(position)
        }
    }

    override fun startUpdate(container: ViewGroup) {
        val i = checkClick()
        if (i != -1) {
            val fragment = listFragment[i]
            if (fragment != null) {
                fragment.calendarAdapter?.notifyDataSetChanged()
            }
        }
        super.startUpdate(container)
    }

    private fun checkClick(): Int {
        val now: Calendar = Calendar.getInstance()
        if (dayClick != null && lastDayClick != null) {
            val click: Calendar = Calendar.getInstance()
            click.time = dayClick
            val lastClick: Calendar = Calendar.getInstance()
            lastClick.time = lastDayClick
            val month = click.get(Calendar.MONTH) - now.get(Calendar.MONTH)
            val year = click.get(Calendar.YEAR) - now.get(Calendar.YEAR)
            val month2 = lastClick.get(Calendar.MONTH) - now.get(Calendar.MONTH)
            val year2 = lastClick.get(Calendar.YEAR) - now.get(Calendar.YEAR)

            val positionClick = startFragment + month + 12 * year
            val positionLastClick = startFragment + month2 + 12 * year2
            if (abs(positionClick - positionLastClick) == 1) {
                return positionLastClick
            }
        }
        return -1
    }
}