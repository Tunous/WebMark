package me.thanel.webmark.ui.content

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_content.*
import m7mdra.com.htmlrecycler.HtmlRecycler
import m7mdra.com.htmlrecycler.adapter.DefaultElementsAdapter
import m7mdra.com.htmlrecycler.source.StringSource
import me.thanel.webmark.R
import me.thanel.webmark.ext.viewModel
import me.thanel.webmark.ui.base.BaseFragment

class ContentFragment : BaseFragment(R.layout.fragment_content) {

    private val viewModel: ContentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.webmark.observe(this, Observer {
            HtmlRecycler.Builder(requireContext())
                .setSource(StringSource(it.content ?: ""))
                .setAdapter(DefaultElementsAdapter(requireContext()) { _, _, _ -> })
                .setRecyclerView(contentRecycler)
                .build()
        })

        viewModel.webmarkId = arguments!!.getLong("webmark-id")
    }
}
