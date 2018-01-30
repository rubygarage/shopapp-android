package com.client.shop.ui.search

import android.content.Context
import android.support.annotation.ColorRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.constraint.ConstraintSet.END
import android.support.constraint.ConstraintSet.START
import android.support.transition.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import com.client.shop.R
import com.client.shop.ext.textChanges
import com.ui.ext.hideKeyboard
import com.ui.ext.showKeyboard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.toolbar_search.view.*
import java.util.concurrent.TimeUnit


class SearchToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    var searchToolbarListener: SearchToolbarListener? = null
    private var isExpanded = false
    private val expandedLineMarginStart: Int
    private val expandedLineMarginEnd: Int
    private val collapsedLineMargin: Int
    private val transitionSet: Transition
    private var searchDisposable: Disposable? = null
    private val searchProcessor: PublishProcessor<String>

    init {
        View.inflate(context, R.layout.toolbar_search, this)
        transitionSet = TransitionSet()
        transitionSet.addTransition(ChangeBounds())
        transitionSet.addTransition(Fade())
        expandedLineMarginStart = resources.getDimensionPixelSize(R.dimen.search_toolbar_expanded_line_margin_start)
        expandedLineMarginEnd = resources.getDimensionPixelSize(R.dimen.search_toolbar_expanded_line_margin_end)
        collapsedLineMargin = resources.getDimensionPixelSize(R.dimen.search_toolbar_collapsed_line_margin)
        searchProcessor = PublishProcessor.create<String>()
        searchIcon.setOnClickListener(this)
        searchInput.setOnClickListener(this)
        clickableArea.setOnClickListener(this)
        setOnClickListener(this)
        clear.setOnClickListener(this)
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchToolbarListener?.onQueryChanged(searchInput.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        searchDisposable = searchInput
            .textChanges()
            .debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    TransitionManager.beginDelayedTransition(this@SearchToolbar)
                    clear.visibility = if (it.isNotEmpty() && isExpanded) View.VISIBLE else View.GONE
                    searchToolbarListener?.onQueryChanged(it)
                },
                { error -> error.printStackTrace() }
            )

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        searchDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    private fun collapseToolbar() {
        searchInput.layoutParams.width = WRAP_CONTENT
        searchInput.setText("")
        searchInput.isFocusableInTouchMode = false
        searchInput.clearFocus()
        searchInput.hideKeyboard()
        searchInput.setHintTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        searchIcon.setImageResource(R.drawable.ic_searchbar)
        clear.visibility = View.GONE
        cartWidget.visibility = View.VISIBLE

        changeBottomLine(R.color.colorBackgroundDark, collapsedLineMargin, collapsedLineMargin)
    }

    private fun expandToolbar() {
        searchInput.layoutParams.width = 0
        searchInput.isFocusableInTouchMode = true
        searchInput.requestFocus()
        searchInput.showKeyboard()
        searchInput.setHintTextColor(ContextCompat.getColor(context, R.color.textColorSecondary))
        searchIcon.setImageResource(R.drawable.ic_arrow_left)
        cartWidget.visibility = View.GONE

        changeBottomLine(R.color.colorPrimary, expandedLineMarginStart, expandedLineMarginEnd)
    }

    private fun changeBottomLine(@ColorRes color: Int, marginStart: Int, marginEnd: Int) {
        bottomLine.setBackgroundResource(color)
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.setMargin(bottomLine.id, START, marginStart)
        constraintSet.setMargin(bottomLine.id, END, marginEnd)
        constraintSet.applyTo(this)
    }

    fun changeToolbarState() {
        TransitionManager.beginDelayedTransition(this, transitionSet)
        if (isExpanded) {
            collapseToolbar()
        } else {
            expandToolbar()
        }
        isExpanded = !isExpanded
        searchToolbarListener?.onToolbarStateChanged(isExpanded)
    }

    fun isToolbarExpanded() = isExpanded

    override fun onClick(view: View) {
        when (view) {
            searchIcon -> changeToolbarState()
            clear -> searchInput.setText("")
            clickableArea -> {
                if (isExpanded) {
                    changeToolbarState()
                }
            }
            is SearchToolbar, searchInput -> {
                if (!isExpanded) {
                    changeToolbarState()
                }
            }
        }
    }

    interface SearchToolbarListener {

        fun onQueryChanged(query: String)

        fun onToolbarStateChanged(isExpanded: Boolean)
    }
}