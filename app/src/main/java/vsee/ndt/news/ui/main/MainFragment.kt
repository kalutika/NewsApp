package vsee.ndt.news.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import vsee.ndt.news.R
import vsee.ndt.news.adapters.ArticleAdapter
import vsee.ndt.news.databinding.MainFragmentBinding
import vsee.ndt.news.models.News
import vsee.ndt.news.repositories.MainRepository
import vsee.ndt.news.repositories.RetrofitService
import vsee.ndt.news.viewmodels.MainViewModel
import vsee.ndt.news.viewmodels.MyViewModelFactory


class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ArticleAdapter
    private lateinit var retrofitService: RetrofitService

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        retrofitService = RetrofitService.getInstance(requireActivity())
        viewModel = ViewModelProvider(
            requireActivity(),
            MyViewModelFactory(MainRepository(retrofitService))
        ).get(MainViewModel::class.java)
        viewModel.articleList.observe(viewLifecycleOwner, Observer {
            adapter.setList(it);
            binding.swipeContainer.isRefreshing = false
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(activity);
        adapter = ArticleAdapter { selectedUser ->
            showDetail(selectedUser)
        }
        binding.recyclerView.adapter = adapter

        binding.swipeContainer.setOnRefreshListener {
            fetchNewData()
        }

        binding.btnSearch.setOnClickListener {
            search(binding.etSearch.text.toString())
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.getArticles()
    }

    private fun showDetail(news: News) {
        viewModel.selectItem(news)
        val activity = activity
        val newsFragment = NewsFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, newsFragment)
            ?.addToBackStack(null)?.commit()
    }

    private fun fetchNewData() {
        adapter.clear()
        viewModel.getArticles()
    }

    private fun search(text: String) {
        adapter.clear()
        viewModel.search(text)
    }
}