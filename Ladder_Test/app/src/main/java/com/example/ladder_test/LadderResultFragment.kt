package com.example.ladder_test

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.ladder_test.databinding.ActivityLadderResultFragmentBinding

class LadderResultFragment: DialogFragment() {
    private lateinit var binding: ActivityLadderResultFragmentBinding
    private var listener: GhostLegResultListener? = null

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityLadderResultFragmentBinding.inflate(inflater, container, false)
        binding.result.text = arguments!!.getString(RESULT_KEY)
        binding.root.setOnClickListener { dismiss() }
        return binding.root
    }

    fun setListener(listener: GhostLegResultListener): LadderResultFragment {
        this.listener = listener
        return this
    }

    /**
     * 코틀린에는 static이 없다.
     * 대체 방법 중 하나인 companion object 방식.
     * @link https://velog.io/@cchloe2311/Kotlin-Kotlin%EC%97%94-static-%ED%82%A4%EC%9B%8C%EB%93%9C%EA%B0%80-%EC%97%86%EB%8B%A4
     */
    companion object {
        /**
         * 'var' vs 'val' vs 'const val'
         * @link https://kumgo1d.tistory.com/60
         */
        const val RESULT_KEY: String = "result"
    }
}

interface GhostLegResultListener {
    fun onCloseGhostLegResult()
}