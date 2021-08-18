package com.example.ladder_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ladder_test.databinding.ActivityLadderBinding

class LadderActivity : AppCompatActivity(), GhostLegInputListener, GhostLegPlayFragmentListener {
    private lateinit var binding: ActivityLadderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLadderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showInputFragment()
    }

    fun showInputFragment() {
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, LadderReadyFragment()).commit()
    }

    override fun onCompleteGhostLegInput(totalCount: Int, playerList: Array<String>, resultList: Array<String>) {
        //프래그먼트 등록
        val bundle = Bundle(1)
        bundle.putInt(LadderPlayFragment.TOTAL_COUNT_KEY, totalCount)
        bundle.putStringArray(LadderPlayFragment.PLAYER_LIST_KEY, playerList)
        bundle.putStringArray(LadderPlayFragment.RESULT_LIST_KEY, resultList)
        val fragment = LadderPlayFragment().setListener(this)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, fragment).commit()
    }

    override fun onRestartGhostLeg() {
        showInputFragment()
    }
}