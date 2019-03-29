package me.thanel.webmark.ui.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_webmark_list.*
import me.drakeet.multitype.MultiTypeAdapter
import me.thanel.webmark.R
import me.thanel.webmark.ext.viewModel
import me.thanel.webmark.ui.base.BaseFragment

class WebmarkListFragment : BaseFragment(R.layout.fragment_webmark_list) {

    private val viewModel: WebmarkListViewModel by viewModel()

    private val adapter = MultiTypeAdapter().apply {
        register(WebmarkViewBinder().apply {
            markAsDone = {
                viewModel.deleteWebmark(it)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webmarkRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        webmarkRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        webmarkRecyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.webmarks.observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }

}
