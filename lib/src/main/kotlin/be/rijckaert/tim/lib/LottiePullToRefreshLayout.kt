package be.rijckaert.tim.lib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class LottiePullToRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : SimplePullToRefreshLayout(context, attrs, defStyle) {

    private var animationFile: Int = -1
    private var animationFileName: String? = null
    private val lottieAnimationView by lazy {
        LottieAnimationView(context).apply {
            if (animationFileName == null) {
                throw IllegalStateException("Could not resolve an animation for your pull to refresh layout")
            }

            setAnimation(animationFileName)
            repeatCount = LottieDrawable.INFINITE
            layoutParams = LayoutParams(ViewGroup.LayoutParams(MATCH_PARENT, triggerOffSetTop)).apply { type = ViewType.TOP_VIEW }
        }
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LottiePullToRefreshLayout, defStyle, 0).let { style ->
            animationFileName = style.getString(R.styleable.LottiePullToRefreshLayout_pull_to_refresh_lottieFile)

            addView(lottieAnimationView)
            style.recycle()
        }

        onProgressListener {
            lottieAnimationView.progress = it
        }

        onTriggerListener {
            lottieAnimationView.resumeAnimation()
        }
    }

    override fun stopRefreshing() {
        super.stopRefreshing()
        lottieAnimationView.pauseAnimation()
    }
}
