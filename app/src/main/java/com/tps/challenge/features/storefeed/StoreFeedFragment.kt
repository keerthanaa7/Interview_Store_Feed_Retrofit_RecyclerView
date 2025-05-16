package com.tps.challenge.features.storefeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tps.challenge.Constants
import com.tps.challenge.R
import com.tps.challenge.TCApplication
import com.tps.challenge.network.model.StoreResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Displays the list of Stores with its title, description and the cover image to the user.
 */
class StoreFeedFragment() : Fragment() {

    companion object {
        const val TAG = "StoreFeedFragment"
    }

    private lateinit var storeFeedAdapter: StoreFeedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        TCApplication.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_store_feed, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_container)
        // Enable if Swipe-To-Refresh functionality will be needed
        swipeRefreshLayout.isEnabled = false
        recyclerView = view.findViewById(R.id.stores_view)
        storeFeedAdapter = StoreFeedAdapter()

        // newly added keer approach 1 use couroutine functions in retrofit service to get data. use live data
        val appcomponent = TCApplication.getAppComponent()

        val tpsCoroutineService = appcomponent.getTPSCoroutineService()

        val storeResponseLiveData: LiveData<List<StoreResponse>> = liveData {
            val storeList = tpsCoroutineService.getStoreFeed(
                Constants.DEFAULT_LATITUDE,
                Constants.DEFAULT_LONGITUDE
            )
            Log.d(TAG, "store list in fragment " + storeList.size)
            // live data gets its value
            emit(storeList)
        }
        storeResponseLiveData.observe(viewLifecycleOwner, Observer {
            val list = it
            Log.d(TAG, "list size  " + list.size)
            storeFeedAdapter.setStoreList(list)
        })

        // newly added keer apprach 2 using normal functions in retrofit service. No need of live data
        /*  val tpsCallService = appcomponent.getTPSCallService()

          var storelist1: List<StoreResponse>? = null
          val tpscall =
              tpsCallService.getStoreFeed(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE)
          tpscall.enqueue(object : Callback<List<StoreResponse>> {
              override fun onResponse(
                  call: Call<List<StoreResponse>>,
                  response: Response<List<StoreResponse>>
              ) {
                  if (response.isSuccessful()) {
                      storelist1 = response.body()
                      storelist1?.let { storeFeedAdapter.setStoreList(it) }
                      System.out.println("list size tps call service " + storelist1?.size)
                  }
              }

              override fun onFailure(call: Call<List<StoreResponse>>, t: Throwable) {
                  Log.d(TAG, "on failure")
              }
          })*/

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            // TODO uncomment the line below whe Adapter is implemented
            adapter = storeFeedAdapter
        }
        return view
    }
}
