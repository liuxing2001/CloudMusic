//package com.jhp.cloudmusic.ui.home.village
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//
//import com.jhp.cloudmusic.databinding.FragmentVillageBinding
//import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
//
//
//class VillageFragment : Fragment() {
//
//    private var _binding: FragmentVillageBinding? = null
//
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val villageViewModel =
//            ViewModelProvider(this)[VillageViewModel::class.java]
//
//        _binding = FragmentVillageBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//        initObserver()
//        return root
//    }
//
//    private fun initObserver() {
//        viewModel.getVillageBannerDj()
//        viewModel.villageBannerLiveData.observe(viewLifecycleOwner) {
//            val item = it.getOrNull()
//            if (item != null) {
//                viewModel.setVillageBanner(item.data)
//                mData = viewModel.djBannerList.map {
//                    BannerItem().apply {
//                        this.imgUrl = it.pic
//                        this.title = "ok"
//                    }
//                }
//
//
//                binding.sibCornerRectangle
//                    .setSource(mData)
//                    .setOnItemClickListener(this)
//                    .startScroll()
//            }
//            // cardAdapter.notifyDataSetChanged()
//
//        }
//    }
//
//
//}