package com.jhp.cloudmusic.ui.home.discovery.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jhp.cloudmusic.databinding.FragmentDiscoveryThreeBinding


class DiscoveryFragmentThree : Fragment() {
    private  var _binding: FragmentDiscoveryThreeBinding? = null
    val binding: FragmentDiscoveryThreeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryThreeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}