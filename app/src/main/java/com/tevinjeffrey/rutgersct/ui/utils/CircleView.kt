package com.tevinjeffrey.rutgersct.ui.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Parcelable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

import com.tevinjeffrey.rutgersct.R

import icepick.Icepick
import icepick.State

class CircleView
@JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0)
  : View(context, attrs, defStyle) {

  init {
    init(attrs, defStyle)
  }

  companion object {
    private val DEFAULT_TITLE_COLOR = Color.CYAN
    private val DEFAULT_TITLE = "Title"
    private val DEFAULT_SHOW_TITLE = true
    private val DEFAULT_TITLE_SIZE = 25f
    private val DEFAULT_BACKGROUND_COLOR = Color.WHITE
    private val DEFAULT_VIEW_SIZE = 96
  }

  @JvmField @field:[State] var titleColor: Int = DEFAULT_TITLE_COLOR
  @JvmField @field:[State] protected var backgroundColor: Int = DEFAULT_BACKGROUND_COLOR
  @JvmField @field:[State] var text: String = DEFAULT_TITLE
  @JvmField @field:[State] var titleSize = DEFAULT_TITLE_SIZE
  @JvmField @field:[State] var showTitle: Boolean = DEFAULT_SHOW_TITLE
  @JvmField @field:[State] var viewSize: Int = 0

  private lateinit var titleTextPaint: TextPaint
  private lateinit var backgroundPaint: Paint

  @State lateinit var innerRectF: RectF

  /**
   * Sets the view's title string attribute value.
   *
   * @param title The example string attribute value to use.
   */
  var titleText: String
    get() = this.text
    set(title) {
      this.text = title
      invalidate()
    }

  override fun onSaveInstanceState(): Parcelable? {
    return Icepick.saveInstanceState(this, super.onSaveInstanceState())
  }

  override fun hasOverlappingRendering(): Boolean {
    return false
  }

  override fun onDraw(canvas: Canvas) {
    innerRectF.set(0f, 0f, viewSize.toFloat(), viewSize.toFloat())

    val centerX = innerRectF.centerX()
    val centerY = innerRectF.centerY()

    canvas.drawCircle(centerX, centerY, (viewSize / 2).toFloat(), backgroundPaint)

    val xPos = centerX.toInt()
    val yPos = (centerY - (titleTextPaint.descent() + titleTextPaint.ascent()) / 2).toInt()

    if (showTitle) {
      canvas.drawText(this.text, xPos.toFloat(), yPos.toFloat(), titleTextPaint)
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    val width = View.resolveSize(DEFAULT_VIEW_SIZE, widthMeasureSpec)
    val height = View.resolveSize(DEFAULT_VIEW_SIZE, heightMeasureSpec)
    viewSize = Math.min(width, height)

    setMeasuredDimension(width, height)
  }

  override fun onRestoreInstanceState(state: Parcelable) {
    super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state))
  }

  fun getBackgroundColor(): Int {
    return backgroundColor
  }

  /**
   * Sets the view's background color attribute value.
   *
   * @param backgroundColor The background color attribute value to use.
   */
  override fun setBackgroundColor(backgroundColor: Int) {
    this.backgroundColor = backgroundColor
    invalidatePaints()
  }

  /**
   * Sets whether the view's title string will be shown.
   *
   * @param flag The boolean value.
   */
  fun setShowTitle(flag: Boolean) {
    this.showTitle = flag
    invalidate()
  }

  private fun init(attrs: AttributeSet?, defStyle: Int) {
    val a = context.obtainStyledAttributes(
        attrs, R.styleable.CircleView, defStyle, 0)

    if (a.hasValue(R.styleable.CircleView_titleText)) {
      this.text = a.getString(R.styleable.CircleView_titleText)
    }

    titleColor = a.getColor(R.styleable.CircleView_titleColor, DEFAULT_TITLE_COLOR)
    backgroundColor = a.getColor(R.styleable.CircleView_backgroundColorValue,
        DEFAULT_BACKGROUND_COLOR)

    titleSize = a.getDimension(R.styleable.CircleView_titleSize, DEFAULT_TITLE_SIZE)

    a.recycle()

    titleTextPaint = TextPaint()
    backgroundPaint = Paint()
    innerRectF = RectF()

    //Title TextPaint
    titleTextPaint.flags = Paint.ANTI_ALIAS_FLAG
    titleTextPaint.isAntiAlias = true
    titleTextPaint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    titleTextPaint.textAlign = Paint.Align.CENTER
    titleTextPaint.isLinearText = true
    titleTextPaint.color = titleColor
    titleTextPaint.textSize = titleSize

    //Background Paint
    backgroundPaint.flags = Paint.ANTI_ALIAS_FLAG
    backgroundPaint.style = Paint.Style.FILL_AND_STROKE
    backgroundPaint.color = backgroundColor
  }

  private fun invalidatePaints() {
    backgroundPaint.color = backgroundColor
    invalidate()
  }
}