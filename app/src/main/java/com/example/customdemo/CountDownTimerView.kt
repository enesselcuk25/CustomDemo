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

class CountDownTimerView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val circlePaint: Paint = Paint()
    private val progressPaint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private var countDownTimer: CustomCountDownTimer? = null
    private var totalTimeInMillis: Long = 0

    init {
        circlePaint.color = Color.RED
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 20f
        circlePaint.isAntiAlias = true

        progressPaint.color = Color.LTGRAY
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = 20f
        progressPaint.isAntiAlias = true

        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 80f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isAntiAlias = true
    }

    fun startTimer(totalTimeInMillis: Long) {
        val minutes = totalTimeInMillis * 1000
        this.totalTimeInMillis = minutes

        countDownTimer = CustomCountDownTimer(minutes, 1000) {
            invalidate()
        }

        countDownTimer?.start()
    }

    private class CustomCountDownTimer(
        millisInFuture: Long,
        countDownInterval: Long,
        private val onTick: () -> Unit
    ) : CountDownTimer(millisInFuture, countDownInterval) {

        private var timePassed: Long = 0
        private var millisInFuture2 = millisInFuture

        override fun onTick(millisUntilFinished: Long) {
            timePassed = millisInFuture2 - millisUntilFinished
            onTick.invoke()
        }

        override fun onFinish() {
            timePassed = millisInFuture2
            onTick.invoke()
        }

        fun getTimeRemaining(): Long {
            return millisInFuture2 - timePassed
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = centerX.coerceAtMost(centerY) - 10f

        // Çemberi çiz
        val oval = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(oval, -90f, 360f, false, circlePaint)

        // Progress'i çiz
        val timeRemaining = countDownTimer?.getTimeRemaining() ?: totalTimeInMillis
        val progressRatio = 1.0 - (timeRemaining.toDouble() / totalTimeInMillis.toDouble()).coerceAtMost(1.0)
        val sweepAngle = (360 * progressRatio).toFloat()
        canvas.drawArc(oval, -90f, sweepAngle, false, progressPaint)

        // Sayacı çiz
        val timeRemaining2 = countDownTimer?.getTimeRemaining() ?: totalTimeInMillis
        val seconds = (timeRemaining2 / 1000 % 60).toInt()
        val timeRemainingText = seconds.toString()
        canvas.drawText(timeRemainingText, centerX, centerY + textPaint.textSize / 4, textPaint)
    }
}


