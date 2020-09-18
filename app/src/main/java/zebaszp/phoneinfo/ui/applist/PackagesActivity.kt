package zebaszp.phoneinfo.ui.applist

import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.databinding.DataBindingUtil
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.databinding.ActivityPackagesBinding
import zebaszp.phoneinfo.domain.PackageInfo

const val LIST_STATE = "infoListState"

class PackagesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPackagesBinding
    private lateinit var infoList : List<PackageInfo>

    var task : LoadPackagesTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packages)

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
        binding.packagesLoading.visibility = View.GONE
        binding.packagesList.visibility = View.VISIBLE
        binding.packagesList.adapter = PackagesRecyclerAdapter(infoList)
    }

    @UiThread
    private fun loadPackagesList() {
        binding.packagesLoading.visibility = View.VISIBLE
        binding.packagesList.visibility = View.GONE
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
