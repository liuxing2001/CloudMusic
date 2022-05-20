package com.jhp.cloudmusic.ui.home.attention

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jhp.cloudmusic.R
import com.jhp.cloudmusic.databinding.FragmentAttentionBinding
import com.jhp.cloudmusic.utils.extension.load
import com.jhp.cloudmusic.utils.extension.toMinAndSeconds
import com.jhp.cloudmusic.data.model.MVList
import com.jhp.cloudmusic.ui.common.adapter.KotlinDataAdapter
import com.jhp.cloudmusic.ui.common.view.LoadingObserver
import com.jhp.cloudmusic.ui.mvplayer.MVPlayerActivity


class AttentionFragment : Fragment() {

    private lateinit var _binding: FragmentAttentionBinding
    private lateinit var cardAdapter: KotlinDataAdapter<MVList.MVDetail>

    private val binding: FragmentAttentionBinding
        get() = _binding

    private val viewModel by lazy {
        ViewModelProvider(this)[AttentionViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attention, container, false);

        _binding = FragmentAttentionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        _binding.vm = viewModel
        initData()
        initObserver()
        return _binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.getMVList()
        viewModel.mvListLiveData.observe(viewLifecycleOwner) {
            val item = it.getOrNull()
            if (item != null) {
                viewModel.setMvList(item.data)
                cardAdapter.notifyDataSetChanged()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner, LoadingObserver(requireContext()))
    }

    private fun initData() {
        //定义外层recyclerview adapter
        cardAdapter = KotlinDataAdapter.Builder<MVList.MVDetail>()
            .setLayoutId(R.layout.item_mv_normal)
            .setData(viewModel.mvList)
            .addBindView { itemView, itemData, index ->
                val name: TextView = itemView.findViewById(R.id.tv_item_mv_name)
                val creator: TextView = itemView.findViewById(R.id.tv_item_mv_creator)
                val image: ImageView = itemView.findViewById(R.id.iv_item_mv_cover)
                val playNum: TextView = itemView.findViewById(R.id.tv_item_mv_playnum)
                val time: TextView = itemView.findViewById(R.id.tv_item_mv_time)
                //数字过大去掉后四位 + 万 大于
                var playCount:String = itemData.playCount.toString()
                if (playCount.length >= 9) {
                    playCount = playCount.substring(0, playCount.length - 8) + "亿"
                } else if (playCount.length >= 5) {
                    playCount = playCount.substring(0, playCount.length - 4) + "万"
                }
                name.text = itemData.name
                creator.text = itemData.artistName
                image.load(itemData.cover)
                playNum.text = playCount
                time.text = itemData.duration.toMinAndSeconds()

                itemView.setOnClickListener {
                    val intent = Intent(context, MVPlayerActivity::class.java).apply {
                        putExtra("id", itemData.id.toString())
                        putExtra("time", time.text)
                        putExtra("title", itemData.name)
                        putExtra("cover",itemData.cover)
                    }
                    startActivity(intent)
                }
            }
            .create()
        //绑定layoutManager、adapter
        binding.attentionRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = cardAdapter
        }
    }

}