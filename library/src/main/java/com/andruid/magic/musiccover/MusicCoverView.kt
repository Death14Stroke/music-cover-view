package com.andruid.magic.musiccover

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.setPadding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

class MusicCoverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {
    private val a =
        context.obtainStyledAttributes(attrs, R.styleable.MusicCoverView, defStyleAttr, 0)

    private val ringColor = a.getColor(R.styleable.MusicCoverView_ringColor, Color.RED)
    private val strokeColor = a.getColor(R.styleable.MusicCoverView_strokeColor, Color.BLACK)
    private val ringWidth = a.getDimension(R.styleable.MusicCoverView_ringWidth, 5f)

    init {
        strokeWidth = a.getDimension(R.styleable.MusicCoverView_strokeWidth, 20f)
        a.recycle()

        shapeAppearanceModel =
            ShapeAppearanceModel.builder(context, 0, R.style.CircleImageView).build()
        setPadding(strokeWidth.toInt())
    }

    private val solidPaint = Paint().apply {
        isAntiAlias = true
        color = strokeColor
        style = Paint.Style.FILL
    }

    private val ringColorPaint = Paint().apply {
        isAntiAlias = true
        color = ringColor
        style = Paint.Style.STROKE
        this.strokeWidth = strokeWidth
    }

    private val rotateAnimator = ObjectAnimator.ofFloat(
        this,
        View.ROTATION, 0f, 360f
    ).apply {
        duration = 5000
        repeatCount = INFINITE
        interpolator = LinearInterpolator()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = measuredWidth / 2f
        val cy = measuredHeight / 2f

        // draw inner solid circle
        canvas.drawCircle(cx, cy, strokeWidth, solidPaint)
        // draw inner thick ring
        ringColorPaint.strokeWidth = strokeWidth
        canvas.drawCircle(cx, cy, 1.5f * strokeWidth, ringColorPaint)
        // draw thin ring near outer border stroke
        ringColorPaint.strokeWidth = ringWidth
        canvas.drawCircle(cx, cy, cx - 2 * strokeWidth, ringColorPaint)
    }

    fun play() {
        if (!rotateAnimator.isStarted)
            rotateAnimator.start()
        else
            rotateAnimator.resume()
    }

    fun pause() {
        rotateAnimator.pause()
    }

    fun isPlaying() = rotateAnimator.isStarted && !rotateAnimator.isPaused
}