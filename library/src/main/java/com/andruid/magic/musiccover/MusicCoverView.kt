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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.properties.Delegates.observable

class MusicCoverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr), LifecycleObserver {
    private val a =
        context.obtainStyledAttributes(attrs, R.styleable.MusicCoverView, defStyleAttr, 0)

    var ringColor: Int by observable(
        a.getColor(
            R.styleable.MusicCoverView_ringColor,
            Color.RED
        )
    ) { _, _, color ->
        ringColorPaint.color = color
        invalidate()
    }

    var strokeColor: Int by observable(
        a.getColor(
            R.styleable.MusicCoverView_strokeColor,
            Color.BLACK
        )
    ) { _, _, color ->
        solidPaint.color = color
        invalidate()
    }

    var ringWidth by observable(
        a.getDimension(
            R.styleable.MusicCoverView_ringWidth,
            5f
        )
    ) { _, _, _ ->
        invalidate()
    }

    init {
        strokeWidth = a.getDimension(R.styleable.MusicCoverView_strokeWidth, 20f)
        a.recycle()

        shapeAppearanceModel =
            ShapeAppearanceModel.builder(context, 0, R.style.CircleImageView).build()
        setPadding(strokeWidth.toInt())

        (context as LifecycleOwner).lifecycle.addObserver(this)
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

    private val rotateAnimator = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f).apply {
        duration = 5000
        repeatCount = INFINITE
        interpolator = LinearInterpolator()
    }

    var isPlaying by observable(false) { _, _, play ->
        if (play)
            play()
        else
            pause()
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

    private fun play() {
        if (!rotateAnimator.isStarted)
            rotateAnimator.start()
        else
            rotateAnimator.resume()
    }

    private fun pause() {
        rotateAnimator.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        if (isPlaying)
            play()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        if (isPlaying)
            pause()
    }
}