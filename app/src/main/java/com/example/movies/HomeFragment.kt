package com.example.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.movies.databinding.FragmentHomeBinding
import com.example.movies.ui.fragments.NowPlayingFragment
import com.example.movies.ui.fragments.PopularFragment
import com.example.movies.ui.fragments.TopRatedFragment
import com.example.movies.ui.fragments.UpComingFragment
import com.example.newsapp.adapters.HomeViewPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding : FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)



        // directing user to search screen
        binding.homeFragmentSearchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_searchFragment)
        }
        val moviesCategoriesFragments = arrayListOf(
            NowPlayingFragment(),
            PopularFragment(),
            TopRatedFragment(),
            UpComingFragment()
        )


        val homePagerAdapter =  HomeViewPagerAdapter(moviesCategoriesFragments,childFragmentManager,lifecycle)

        binding.mainActivityViewPager.adapter = homePagerAdapter

        TabLayoutMediator(binding.mainActivityTabLayout,binding.mainActivityViewPager){tab,position ->
            when(position){
                0 -> tab.text ="NowPlaying"
                1 -> tab.text = "Popular"
                2 -> tab.text = "TopRated"
                3 -> tab.text = "UpComing"
            }

        }.attach()

    }

}