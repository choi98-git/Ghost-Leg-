package com.example.ladder_test

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ladder_test.databinding.ActivityLadderPlayFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//import com.innocent.ghostleg.databinding.FragmentGhostLegPlayBinding
//import com.innocent.ghostleg.drawlots.DrawLotsPlayFragment
//import com.innocent.ghostleg.ladder.Ladder
//import com.innocent.ghostleg.ladder.Line

class LadderPlayFragment: Fragment(), GhostLegResultListener {
    private lateinit var binding: ActivityLadderPlayFragmentBinding
    private var listener: GhostLegPlayFragmentListener? = null

    var btnCount = 0
    private val CANVAS_SIZE: Float = 1000f
    private val BITMAP_MARGIN: Float = 120f
    private val BITMAP_SIZE: Int = (CANVAS_SIZE + BITMAP_MARGIN * 2).toInt()

    private val STROKE_WIDTH: Float = 20f
    private val COLOR_PRESET: Array<Int> = arrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.YELLOW, Color.DKGRAY)

    private val ladder: Ladder = Ladder()
    private var bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888)
    private var canvas = Canvas(bitmap)
    private var count = 0


    fun drawLadder(players: Array<String>, results: Array<String>) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 50f;
        paint.setColor(Color.BLACK)
        paint.strokeWidth = STROKE_WIDTH

        drawLines(ladder.getVerticalLines(), paint)
        drawLines(ladder.getHorizontalLines(), paint)

        players.forEachIndexed { index, s -> canvas.drawText(s, index * getWidthRatio(count) - 50f, -10f, paint) }
        results.forEachIndexed { index, s ->
            val a = canvas.drawText(s, index * getWidthRatio(count) - 50f, 1050f, paint)
        }
    }


    fun drawLines(lines: ArrayList<Line>, paint: Paint) {
        lines.forEach {
            canvas.drawLine(
                it.pt1.x.toFloat() * getWidthRatio(count),
                it.pt1.y.toFloat() * getHeightRatio(),
                it.pt2.x.toFloat() * getWidthRatio(count),
                it.pt2.y.toFloat() * getHeightRatio()
                , paint)
        }
    }

    fun howDraw(num:Int){
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val alpha = 150
        paint.strokeWidth = STROKE_WIDTH

        paint.setColor(COLOR_PRESET[num % 7])
        paint.alpha = alpha
        drawPP(ladder.getPoints(num), paint)
    }


    fun allDrawPoints() {
        for (i in 0 until count) {
            howDraw(i)
        }
    }

    fun oneDrawPoints(position: Int){
        howDraw(position)
    }

    fun drawPP(points: ArrayList<Point>, paint: Paint) {
        val floatPoints = points.map{PointF(it.x.toFloat() * getWidthRatio(count), it.y.toFloat() * getHeightRatio())}
        val floatArray = FloatArray((points.size - 1) * 4)

        for (i in 0 until points.size - 1) {
            floatArray[i * 4] = floatPoints[i].x
            floatArray[i * 4 + 1] = floatPoints[i].y
            floatArray[i * 4 + 2] = floatPoints[i + 1].x
            floatArray[i * 4 + 3] = floatPoints[i + 1].y
        }

        canvas.drawLines(floatArray, paint)
    }

    fun getWidthRatio(count: Int): Float {
        return CANVAS_SIZE / (count - 1)
    }

    fun getHeightRatio(): Float {
        return CANVAS_SIZE / 20
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityLadderPlayFragmentBinding.inflate(inflater, container, false)

        count = arguments!!.getInt(LadderPlayFragment.TOTAL_COUNT_KEY)
        val players = arguments!!.getStringArray(LadderPlayFragment.PLAYER_LIST_KEY) as Array<String>
        val results = arguments!!.getStringArray(LadderPlayFragment.RESULT_LIST_KEY) as Array<String>
        ladder.initializeLadder(count)
        canvas.translate(BITMAP_MARGIN, BITMAP_MARGIN)
        binding.canvas.setImageBitmap(bitmap)
        drawLadder(players, results)

        binding.buttonDraw.setOnClickListener {
            allDrawPoints();
            restart()
        }

        binding.oneDrawButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (btnCount == count ) {
                    btnCount = 0
                }

                else{
                    drawLadder(players, results)
                    delay(100)
                    oneDrawPoints(btnCount)
                    btnCount++
                    if(btnCount == count -1){
                        restart()
                    }
                }

            }
        }
        return binding.root
    }

    override fun onCloseGhostLegResult() {
        binding.restartButton.visibility = View.VISIBLE

    }

    fun setListener(listener: GhostLegPlayFragmentListener): LadderPlayFragment {
        this.listener = listener
        return this
    }

    fun restart(){
        binding.buttonDraw.text = "다시하기"
        binding.buttonDraw.setOnClickListener {
            startActivity(Intent(this.context, LadderActivity::class.java))
        }
    }

    companion object {
        const val TOTAL_COUNT_KEY: String = "total_count"
        const val PLAYER_LIST_KEY: String = "player_list"
        const val RESULT_LIST_KEY: String = "result_list"
    }
}

interface GhostLegPlayFragmentListener {
    fun onRestartGhostLeg()
}