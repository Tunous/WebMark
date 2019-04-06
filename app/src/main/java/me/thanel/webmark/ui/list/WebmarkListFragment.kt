package me.thanel.webmark.ui.list

import android.content.ClipDescription
import android.content.ClipboardManager
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_webmark_list.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.thanel.recyclerviewutils.adapter.lazyAdapterWrapper
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isRead
import me.thanel.webmark.ext.viewModel
import me.thanel.webmark.preference.Preference
import me.thanel.webmark.preference.PreferenceKey
import me.thanel.webmark.ui.base.BaseFragment
import me.thanel.webmark.ui.touchhelper.ItemTouchCallback
import me.thanel.webmark.work.SaveWebmarkWorker
import org.kodein.di.generic.instance

class WebmarkListFragment : BaseFragment(R.layout.fragment_webmark_list) {

    private val viewModel: WebmarkListViewModel by viewModel()
    private val clipboard: ClipboardManager by instance()
    private val inputMethodManager: InputMethodManager by instance()
    private val latestSuggestedUrlPreference: Preference<String> by instance(tag = PreferenceKey.LatestSuggestedUrl)

    private val adapterWrapper by lazyAdapterWrapper {
        register(WebmarkViewBinder(), WebmarkItemCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webmarkRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        webmarkRecyclerView.adapter = adapterWrapper.adapter

        val itemTouchCallback = ItemTouchCallback.create { position, direction ->
            val item = adapterWrapper.adapter.items.getOrNull(position)
            when (item) {
                is Webmark -> onSwiped(direction, item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(webmarkRecyclerView)

        filterInput.doAfterTextChanged {
            viewModel.filterText = it?.toString() ?: ""
        }

        archiveCheckBox.onCheckedChanged = { isChecked ->
            viewModel.showArchive = isChecked
        }

        view.findViewById<MotionLayout>(R.id.toolbar)
            .setTransitionListener(object : TransitionAdapter() {
                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if (currentId == R.id.start) {
                        searchIcon.setImageResource(R.drawable.ic_search)
                        filterInput.clearFocus()
                        filterInput.text.clear()
                    } else {
                        searchIcon.setImageResource(R.drawable.ic_close)
                        filterInput.requestFocus()
                    }
                }
            })

        filterInput.onFocusChangeListener = View.OnFocusChangeListener { focusView, hasFocus ->
            if (hasFocus) {
                inputMethodManager.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT)
            } else {
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.unreadWebmarks.observe(this, Observer {
            launch(Dispatchers.IO) {
                adapterWrapper.updateItems(it)
            }
            updateEmptyView(it.isEmpty())
        })

        if (savedInstanceState == null) {
            suggestSaveCopiedUrl()
        }
    }

    private fun updateEmptyView(shouldShow: Boolean) {
        noWebmarksView.isVisible = shouldShow
        if (!shouldShow) return

        emptyViewTitleView.setText(
            if (filterInput.text.isNullOrBlank()) {
                if (archiveCheckBox.isChecked) {
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

        val latestSuggestedUrl = latestSuggestedUrlPreference.value
        if (latestSuggestedUrl == text) return

        launch {
            val uri = Uri.parse(text)
            if (viewModel.isSaved(uri)) return@launch

            val message = getString(R.string.question_save_copied_url, text)
            Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_save) {
                    SaveWebmarkWorker.enqueue(uri)
                }
                .show()

            latestSuggestedUrlPreference.value = text
        }
    }

    private fun onSwiped(direction: Int, item: Webmark) {
        if (item.isRead) {
            if (direction == ItemTouchHelper.RIGHT) {
                delete(item.id)
            } else {
                viewModel.markWebmarkAsUnread(item.id)
            }
            return
        }

        if (direction == ItemTouchHelper.RIGHT) {
            markAsRead(item.id)
        }
    }

    private fun markAsRead(id: Long) {
        viewModel.markWebmarkAsRead(id)

        Snackbar.make(coordinator, R.string.info_marked_read, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo) { viewModel.markWebmarkAsUnread(id) }
            .show()
    }

    private fun delete(id: Long) {
        viewModel.deleteWebmark(id)
        Snackbar.make(coordinator, R.string.info_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo) { viewModel.undoDeleteWebmark(id) }
            .show()
    }

}
