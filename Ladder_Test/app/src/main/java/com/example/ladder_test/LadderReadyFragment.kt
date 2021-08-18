package com.example.ladder_test

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.ladder_test.databinding.ActivityLadderReadyFragmentBinding

class LadderReadyFragment : Fragment() {
    private lateinit var binding: ActivityLadderReadyFragmentBinding
    private var listener: GhostLegInputListener? = null
    private var playerCount: Int = MIN_MEMBER_COUNT
    private lateinit var playerListAdapter: LadderAdapter
    private lateinit var resultListAdapter: LadderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityLadderReadyFragmentBinding.inflate(inflater, container, false)

        initPlayListView()

        binding.btnPlay.setOnClickListener {
            listener?.onCompleteGhostLegInput(
                Integer.parseInt(binding.memberCounter.tvNum.text.toString()),
                (playerListAdapter.dataList.map { data -> data.name }).toTypedArray(),
                (resultListAdapter.dataList.map { data -> data.name }).toTypedArray()
            )
        }
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initPlayListView() {
        binding.memberCounter.tvTitle.text = "참가 인원"
        playerListAdapter = LadderAdapter(context!!, arrayListOf())
        resultListAdapter = LadderAdapter(context!!, arrayListOf())
        binding.playerListView.adapter = playerListAdapter
        binding.resultListView.adapter = resultListAdapter
        binding.memberCounter.tvTitle.text = "당첨 인원"

        binding.memberCounter.btnIncrease.setOnClickListener {
            if (playerCount < MAX_MEMBER_COUNT) {
                playerCount++
                updateView()
            }
        }
        binding.memberCounter.btnDecrease.setOnClickListener {
            if (playerCount > MIN_MEMBER_COUNT) {
                playerCount--
                updateView()
            }
        }

        updateView()
    }


    private fun updateView(beforeCount: Int = playerListAdapter.count) {
        binding.memberCounter.tvNum.text = playerCount.toString()
        if (beforeCount < playerCount) {
            for (i in beforeCount until playerCount) {
                val view: View = LayoutInflater.from(context).inflate(R.layout.eidttext_list, null)
                val editText = view.findViewById<EditText>(R.id.ed_name)
                playerListAdapter.addItem(Data(i + 1, editText.text.toString()))
                resultListAdapter.addItem(Data(i + 1, editText.text.toString()))
            }
        } else if (beforeCount > playerCount) {
            for (i in playerCount until beforeCount) {
                playerListAdapter.removeItem()
                resultListAdapter.removeItem()
            }
        }
    }

    // 뷰에 텍스트 보여줌
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GhostLegInputListener) {
            listener = context
        }
    }

    companion object {
        const val MAX_MEMBER_COUNT: Int = 8
        const val MIN_MEMBER_COUNT: Int = 2
    }
}

interface GhostLegInputListener {
    fun onCompleteGhostLegInput(totalCount: Int, playerList: Array<String>, resultList: Array<String>)
}