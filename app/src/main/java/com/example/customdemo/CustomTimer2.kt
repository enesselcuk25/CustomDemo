package com.example.customdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class BaseTimer(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val circlePaint: Paint = Paint()
    private val progressPaint: Paint = Paint()
    private val progressPaint2: Paint = Paint()
    private val textPaint: Paint = Paint()
    private var sweepAngle: Float = 0f
    private val maxSweepAngle = 360f
    private var animationDuration: Long = 0
    private var remainingTime: Long = 0
    private var passing: Float = 0f

    private var countDownTimer: CountDownTimer? = null

    var onFinishListener: (() -> Unit)? = null

    private var startAngle: Float = -90f  // Başlangıç açısı

    init {
        circlePaint.color = MaterialColors.getColor(context,R.attr.defaultLineProgressTimerColor,null)
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 10f
        circlePaint.isAntiAlias = true

        progressPaint.color = MaterialColors.getColor(context,R.attr.defaultLineProgressColor,null)
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = 10f
        progressPaint.isAntiAlias = true

        progressPaint2.color = MaterialColors.getColor(context,R.attr.defaultLineProgressColor,null)
        progressPaint2.style = Paint.Style.STROKE
        progressPaint2.strokeWidth = 10f
        progressPaint2.isAntiAlias = true

        textPaint.color = MaterialColors.getColor(context,R.attr.defaultLineProgressTimerColor,null)
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 80f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isAntiAlias = true

    }

    fun startTimer(timeToExpire: Long, sumTimer: Long) {
        remainingTime = timeToExpire
        animationDuration = sumTimer * 1000

        val startProgress = sumTimer - timeToExpire
        startAngle = -90f + (startProgress.toFloat() / sumTimer.toFloat()) * maxSweepAngle
        sweepAngle = (startProgress.toFloat() / sumTimer.toFloat()) * maxSweepAngle

        val normalizedStartProgress = startProgress.toFloat() / sumTimer.toFloat()
        val valueBetweenZeroAndMaxSweep = normalizedStartProgress * maxSweepAngle
        passing = -valueBetweenZeroAndMaxSweep

        countDownTimer = object : CountDownTimer(animationDuration, 16) {
            private val startTime = SystemClock.elapsedRealtime()

            override fun onTick(millisUntilFinished: Long) {
                val elapsedTime = SystemClock.elapsedRealtime() - startTime
                val progress = elapsedTime.toFloat() / animationDuration

                if (elapsedTime <= timeToExpire * 1000) {
                    sweepAngle = maxSweepAngle * progress
                    remainingTime = (timeToExpire - (elapsedTime / 1000))

                } else {
                    if (sweepAngle < maxSweepAngle) {
                        sweepAngle = maxSweepAngle
                    }
                    remainingTime = 0
                    countDownTimer?.cancel()
                    onFinishListener?.invoke()

                }
                postInvalidateOnAnimation()
            }

            override fun onFinish() {
                onFinishListener?.invoke()
            }
        }
    }

    fun stopTimer() = countDownTimer?.cancel()

    fun timerStart() = countDownTimer?.start()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = centerX.coerceAtMost(centerY) - 10f

        val oval = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // circlePaint ile çemberi çiz
        val ab = maxSweepAngle - startAngle - 90

        canvas.drawArc(oval, startAngle, ab, false, circlePaint)

        // progressPaint ile progress çizgisini çiz
        canvas.drawArc(oval, startAngle, sweepAngle, false, progressPaint)

        // servisten gelen sn gri yap
        canvas.drawArc(oval, startAngle, passing, false, progressPaint2)

        val secondsText = "$remainingTime"
        canvas.drawText(secondsText, centerX, centerY + textPaint.textSize / 4, textPaint)
    }
}




