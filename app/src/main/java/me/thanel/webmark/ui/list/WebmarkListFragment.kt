package me.thanel.webmark.ui.list

import android.content.ClipDescription
import android.content.ClipboardManager
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.TooltipCompat
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_webmark_list.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.thanel.recyclerviewutils.adapter.lazyAdapterWrapper
import me.thanel.webmark.R
import me.thanel.webmark.action.WebmarkActionHandler
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.share
import me.thanel.webmark.ext.updateTheme
import me.thanel.webmark.ext.viewModel
import me.thanel.webmark.preferences.WebMarkPreferences
import me.thanel.webmark.ui.base.BaseFragment
import me.thanel.webmark.ui.imageloader.ImageLoader
import me.thanel.webmark.ui.touchhelper.CollapseItemAnimator
import me.thanel.webmark.ui.touchhelper.ItemTouchCallback
import me.thanel.webmark.ui.touchhelper.SwipeableViewHolder
import me.thanel.webmark.ui.touchhelper.WebmarkAction
import me.thanel.webmark.work.ExtractWebmarkDetailsWorker
import me.thanel.webmark.work.SaveWebmarkWorker
import org.kodein.di.generic.instance

class WebmarkListFragment : BaseFragment(R.layout.fragment_webmark_list), WebmarkActionHandler {

    private val viewModel: WebmarkListViewModel by viewModel()
    private val clipboard: ClipboardManager by instance()
    private val inputMethodManager: InputMethodManager by instance()
    private val imageLoader: ImageLoader by instance()

    private val adapterWrapper by lazyAdapterWrapper {
        register(WebmarkViewBinder(this@WebmarkListFragment, imageLoader), WebmarkItemCallback)
    }

    private val toolbarLayout by lazy { toolbar as MotionLayout }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webmarkRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        webmarkRecyclerView.adapter = adapterWrapper.adapter

        val itemTouchCallback = ItemTouchCallback.create { viewHolder, position, direction ->
            (viewHolder as? SwipeableViewHolder)?.shouldAnimateCollapsing = true
            when (val item = adapterWrapper.adapter.items.getOrNull(position)) {
                is Webmark -> onSwiped(direction, item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(webmarkRecyclerView)

        webmarkRecyclerView.itemAnimator = CollapseItemAnimator()

        searchInputView.doAfterTextChanged {
            viewModel.filterText = it?.toString() ?: ""
        }

        updateArchiveButtonTooltip()
        archiveToggleButton.isChecked = viewModel.showArchive
        archiveToggleButton.onCheckedChanged = { isChecked ->
            viewModel.showArchive = isChecked
            updateArchiveButtonTooltip()
        }

        updateThemeButtonTooltip()
        themeToggleButton.isChecked = WebMarkPreferences.useDarkTheme
        themeToggleButton.onCheckedChanged = { useDarkTheme ->
            WebMarkPreferences.useDarkTheme = useDarkTheme
            updateThemeButtonTooltip()
            updateTheme(useDarkTheme)
        }

        updateSearchButtonTooltip()
        toolbarLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                updateSearchButtonTooltip(currentId)
                if (currentId == R.id.defaultToolbarState) {
                    searchToggleButton.setImageResource(R.drawable.ic_search)
                    searchInputView.clearFocus()
                    searchInputView.text.clear()
                } else {
                    searchToggleButton.setImageResource(R.drawable.ic_close)
                    searchInputView.requestFocus()
                }
            }
        })

        searchInputView.onFocusChangeListener = View.OnFocusChangeListener { focusView, hasFocus ->
            if (hasFocus) {
                inputMethodManager.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT)
            } else {
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.unarchivedWebmarks.observe(this, Observer {
            launch(Dispatchers.IO) {
                adapterWrapper.updateItems(it)
            }
            updateEmptyView(it.isEmpty())
        })

        if (savedInstanceState == null) {
            suggestSaveCopiedUrl()
        }
    }

    private fun updateSearchButtonTooltip(state: Int = toolbarLayout.currentState) {
        val searchButtonTooltip =
            if (state == R.id.defaultToolbarState) getString(R.string.action_search)
            else getString(R.string.action_close_search)
        TooltipCompat.setTooltipText(searchToggleButton, searchButtonTooltip)
    }

    private fun updateArchiveButtonTooltip() {
        val archiveButtonTooltip =
            if (viewModel.showArchive) getString(R.string.action_hide_archive)
            else getString(R.string.action_show_archive)
        TooltipCompat.setTooltipText(archiveToggleButton, archiveButtonTooltip)
    }

    private fun updateThemeButtonTooltip() {
        val themeButtonTooltip =
            if (WebMarkPreferences.useDarkTheme) getString(R.string.action_use_light_theme)
            else getString(R.string.action_use_dark_theme)
        TooltipCompat.setTooltipText(themeToggleButton, themeButtonTooltip)
    }

    private fun updateEmptyView(shouldShow: Boolean) {
        noWebmarksView.isVisible = shouldShow
        if (!shouldShow) return

        emptyViewTitleView.setText(
            if (searchInputView.text.isNullOrBlank()) {
                if (archiveToggleButton.isChecked) {
                    R.string.title_nothing_archived
                } else {
                    R.string.title_nothing_saved
                }
            } else {
                R.string.title_nothing_found
            }
        )
    }

    private fun suggestSaveCopiedUrl() {
        val hasTextMimeType =
            clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                ?: false
        if (!clipboard.hasPrimaryClip() || !hasTextMimeType) return

        val item = clipboard.primaryClip?.getItemAt(0) ?: return
        val text = item.text.toString()
        if (!Patterns.WEB_URL.matcher(text).matches()) return

        if (WebMarkPreferences.latestSuggestedUrl == text) return

        launch {
            val uri = Uri.parse(text)
            if (viewModel.isSaved(uri)) return@launch

            val message = getString(R.string.question_save_copied_url, text)
            Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_save) {
                    SaveWebmarkWorker.enqueue(uri)
                }
                .show()

            WebMarkPreferences.latestSuggestedUrl = text
        }
    }

    private fun onSwiped(direction: Int, webmark: Webmark) {
        val action = WebmarkAction.swipeInDirectionFor(direction, webmark) ?: return
        performAction(action, webmark)
    }

    private fun archive(id: Long) {
        viewModel.archiveWebmark(id)

        Snackbar.make(coordinator, R.string.info_archived, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo) { viewModel.unarchiveWebmark(id) }
            .show()
    }

    private fun unarchive(id: Long) {
        viewModel.unarchiveWebmark(id)

        Snackbar.make(coordinator, R.string.info_unarchived, Snackbar.LENGTH_SHORT)
            .setAction(R.string.action_undo) { viewModel.archiveWebmark(id) }
            .show()
    }

    private fun delete(id: Long) {
        viewModel.deleteWebmark(id)
        Snackbar.make(coordinator, R.string.info_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo) { viewModel.undoDeleteWebmark(id) }
            .show()
    }

    override fun performAction(action: WebmarkAction, webmark: Webmark) {
        when (action) {
            WebmarkAction.Archive -> archive(webmark.id)
            WebmarkAction.Delete -> delete(webmark.id)
            WebmarkAction.ShareLink -> requireContext().share(webmark.url)
            WebmarkAction.Unarchive -> unarchive(webmark.id)
            WebmarkAction.ExtractDetails -> ExtractWebmarkDetailsWorker.enqueue(webmark.id)
        }
    }

}
