package com.example.newsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.movies.ui.fragments.NowPlayingFragment
import com.example.movies.ui.fragments.PopularFragment
import com.example.movies.ui.fragments.TopRatedFragment
import com.example.movies.ui.fragments.UpComingFragment


class HomeViewPagerAdapter(
    private val fragments :List<Fragment>,
    fm: FragmentManager,
    lifeCycle: Lifecycle
    ) : FragmentStateAdapter(fm,lifeCycle){


        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
