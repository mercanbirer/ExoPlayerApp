package com.example.ExoPlayerApp.player

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.ExoPlayerApp.R
import com.example.ExoPlayerApp.adapter.LanguageAdapter
import com.example.ExoPlayerApp.base.BaseFragment
import com.example.ExoPlayerApp.databinding.FragmentExoPlayerBinding
import com.example.ExoPlayerApp.model.Language
import com.example.ExoPlayerApp.subtitle.Subtitle
import com.example.ExoPlayerApp.util.ExoUtil
import com.example.ExoPlayerApp.util.ExoUtilHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.exo_player_control_view.*
import java.io.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ExoPlayerFragment : BaseFragment<FragmentExoPlayerBinding>(R.layout.fragment_exo_player) {

    @Inject
    lateinit var exoUtil: ExoUtil
    private lateinit var languageAdapter: LanguageAdapter
    var subtitles: ArrayList<Subtitle>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exo = binding.exoPlayerView
        exoUtil.setPlayerView(exo)
        exoUtil.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")

        viewLifecycleOwner.lifecycle.addObserver(ExoUtilHandler(exoUtil))

        val item1 = Language("Türkçe")
        val item2 = Language("İngilizce")
        val item3 = Language("Arapça")
        val list = arrayListOf(item1, item2, item3)
        languageAdapter = LanguageAdapter(requireContext(), list)
        selectionListView.adapter = languageAdapter


        playerSubtitle.setOnClickListener {
            selectionListView.visibility = View.VISIBLE

            selectionListView.setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {
                        Log.e("TÜRKÇE", "GELDİ")
                        if (subtitles == null) return@setOnItemClickListener
                    }
                    1 -> {
                        Log.e("İngilizce", "GELDİ")
                    }
                    2 -> {
                        Log.e("kşsdlsf", "GELDİ")

                    }
                }
            }
        }


    }

}