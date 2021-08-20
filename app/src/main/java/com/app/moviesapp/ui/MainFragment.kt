package com.app.moviesapp.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.moviesapp.R
import com.app.moviesapp.data.model.Movie
import com.app.moviesapp.ui.viewmodel.MainViewModel
import com.app.moviesapp.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

@AndroidEntryPoint
class MainFragment : Fragment(), MainAdapter.OnMovieClickListener {

    private lateinit var adapter: MainAdapter

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.fetchMoviesList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    rv_movies.adapter =
                        MainAdapter(requireContext(), result.data.toMutableList(), this)
                    adapter = rv_movies.adapter as MainAdapter
                }
                is Resource.Failure -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error getting data ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.fetchMoviesFilterList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    rv_movies.adapter =
                        MainAdapter(requireContext(), result.data.toMutableList(), this)
                    adapter = rv_movies.adapter as MainAdapter
                }
                is Resource.Failure -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error getting data ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }



    private fun setupRecyclerView() {
        rv_movies.layoutManager = LinearLayoutManager(requireContext())
        rv_movies.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onMovieCLick(movie: Movie, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        findNavController().navigate(R.id.action_mainFragment_to_movieDetailFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_favorites ->
                findNavController().navigate(R.id.action_mainFragment_to_favoritesFragment)

            R.id.menu_about ->
                findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
            else -> return true
        }

        return super.onOptionsItemSelected(item)
    }


}