package zebaszp.phoneinfo.ui.applist

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.PackageInfo

const val LIST_STATE = "infoListState"

class PackagesActivity : AppCompatActivity() {

    private lateinit var loading: ProgressBar
    private lateinit var recycler: RecyclerView
    private lateinit var infoList: List<PackageInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)

        loading = findViewById(R.id.packages_loading)
        recycler = findViewById(R.id.packages_list)

        val packages = savedInstanceState?.getParcelableArrayList(LIST_STATE) ?: listOf<PackageInfo>()

        if (packages.isNotEmpty())
            showPackages(packages)
        else
            loadPackagesList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(LIST_STATE, ArrayList(infoList))
    }

    @UiThread
    private fun showPackages(packages: List<PackageInfo>) {
        infoList = packages
        loading.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        recycler.adapter = PackagesRecyclerAdapter(packages)
    }

    @UiThread
    private fun loadPackagesList() {
        loading.visibility = View.VISIBLE
        recycler.visibility = View.GONE

        lifecycleScope.launchWhenResumed {
            val pm = packageManager
            showPackages(withContext(Dispatchers.IO) {
                val items = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                items.map {
                    PackageInfo(pm.getApplicationLabel(it).toString(), it.packageName)
                }
            })
        }
    }
}