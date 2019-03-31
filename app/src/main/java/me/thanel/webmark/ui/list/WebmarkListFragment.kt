package me.thanel.webmark.ui.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_webmark_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.thanel.recyclerviewutils.adapter.lazyAdapterWrapper
import me.thanel.webmark.R
import me.thanel.webmark.ext.viewModel
import me.thanel.webmark.ui.base.BaseFragment

class WebmarkListFragment : BaseFragment(R.layout.fragment_webmark_list) {

    private val viewModel: WebmarkListViewModel by viewModel()

    private val adapterWrapper by lazyAdapterWrapper {
        register(WebmarkViewBinder().apply {
            markAsRead = ::markAsRead
        }, WebmarkItemCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webmarkRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        webmarkRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        webmarkRecyclerView.adapter = adapterWrapper.adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.unreadWebmarks.observe(this, Observer {
            GlobalScope.launch(Dispatchers.IO) {
                adapterWrapper.updateItems(it)
            }
        })
    }

    private fun markAsRead(id: Long) {
        viewModel.markWebmarkAsRead(id)

        Snackbar.make(webmarkRecyclerView, R.string.info_marked_read, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo) { viewModel.markWebmarkAsUnread(id) }
            .show()
    }

}
