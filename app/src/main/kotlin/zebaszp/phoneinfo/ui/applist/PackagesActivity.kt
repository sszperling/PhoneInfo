package zebaszp.phoneinfo.ui.applist

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.databinding.ActivityPackagesBinding
import zebaszp.phoneinfo.domain.PackageInfo

const val LIST_STATE = "infoListState"

class PackagesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPackagesBinding
    private lateinit var infoList : List<PackageInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packages)

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
        binding.packagesLoading.visibility = View.GONE
        binding.packagesList.visibility = View.VISIBLE
        binding.packagesList.adapter = PackagesRecyclerAdapter(infoList)
    }

    @UiThread
    private fun loadPackagesList() {
        binding.packagesLoading.visibility = View.VISIBLE
        binding.packagesList.visibility = View.GONE
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
