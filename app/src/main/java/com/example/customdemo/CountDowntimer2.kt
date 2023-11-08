package com.garantibbva.authsignsdk.uicomponents.components

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
import com.garantibbva.authsignsdk.R

class BaseTimer2(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val circlePaint: Paint = Paint()
    private val progressPaint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private var sweepAngle: Float = 0f
    private val maxSweepAngle = 360f
    private var animationDuration: Long = 0 // 5 saniye (ayarlayabilirsiniz)
    private var elapsedSeconds: Long = 0

    private var countDownTimer: CustomCountDownTimer? = null

    var onFinishListener: (() -> Unit)? = null

    init {
        circlePaint.color = ContextCompat.getColor(context, R.color.defaultAccentColor)
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 10f
        circlePaint.isAntiAlias = true

        progressPaint.color = Color.LTGRAY
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = 10f
        progressPaint.isAntiAlias = true

        textPaint.color = ContextCompat.getColor(context, R.color.defaultAccentColor)
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 80f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isAntiAlias = true
    }

    fun startTimer(totalSeconds: Long) {
        val milliseconds = totalSeconds * 1000
        this.animationDuration = milliseconds
         countDownTimer = CustomCountDownTimer(animationDuration,{ progress ->
                sweepAngle = maxSweepAngle * progress
                elapsedSeconds = (totalSeconds - (progress * totalSeconds)).toLong()
                postInvalidateOnAnimation()

        },{
            onFinishListener?.invoke()
        })

        countDownTimer?.start()
    }


    fun stopTimer() {
        countDownTimer?.cancel()
    }

    private class CustomCountDownTimer(
        private val duration: Long,
        private val onTick: (progress: Float) -> Unit,
        private val onFinish: () -> Unit,
    ) : CountDownTimer(duration, 16) {

        override fun onTick(millisUntilFinished: Long) {
            val progress = (duration - millisUntilFinished).toFloat() / duration
            onTick(progress)
        }

        override fun onFinish() {
            onTick(1f)
            onFinish.invoke()
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = centerX.coerceAtMost(centerY) - 10f

        val oval = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(oval, -90f, maxSweepAngle, false, circlePaint)
        canvas.drawArc(oval, -90f + sweepAngle, maxSweepAngle - sweepAngle, false, progressPaint)

        val secondsText = "$elapsedSeconds"
        canvas.drawText(secondsText, centerX, centerY + textPaint.textSize / 4, textPaint)
    }
}



