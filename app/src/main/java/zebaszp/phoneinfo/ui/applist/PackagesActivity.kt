package zebaszp.phoneinfo.ui.applist

import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.PackageInfo

const val LIST_STATE = "infoListState"

class PackagesActivity : AppCompatActivity() {

    private lateinit var loading: ProgressBar
    private lateinit var recycler : RecyclerView
    private lateinit var infoList : List<PackageInfo>

    var task : LoadPackagesTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)

        loading = findViewById(R.id.packages_loading)
        recycler = findViewById(R.id.packages_list)

        infoList = savedInstanceState?.getParcelableArrayList(LIST_STATE) ?: ArrayList()

        if(infoList.isNotEmpty())
            showPackages()
        else
            loadPackagesList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(task == null) {
            outState.putParcelableArrayList(LIST_STATE, ArrayList(infoList))
        }
    }

    private fun showPackages() {
        task = null
        loading.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        recycler.adapter = PackagesRecyclerAdapter(infoList)
    }

    @UiThread
    private fun loadPackagesList() {
        loading.visibility = View.VISIBLE
        recycler.visibility = View.GONE
        task = LoadPackagesTask(packageManager) {
            infoList = it
            showPackages()
        }
        task!!.execute()
    }

}

class LoadPackagesTask(val pm: PackageManager, val delegate: (List<PackageInfo>) -> Unit)
    : AsyncTask<Void, Void, List<PackageInfo>>() {

    override fun doInBackground(vararg params: Void?): List<PackageInfo> {
        val items = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        return items.map {
            PackageInfo(pm.getApplicationLabel(it).toString(), it)
        }
    }

    override fun onPostExecute(result: List<PackageInfo>) {
        delegate(result)
    }
}