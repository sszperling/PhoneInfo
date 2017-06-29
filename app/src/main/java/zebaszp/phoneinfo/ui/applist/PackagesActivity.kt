package zebaszp.phoneinfo.ui.applist

import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.OrientationHelper
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentInfo
import com.facebook.litho.LithoView
import com.facebook.litho.widget.Card
import com.facebook.litho.widget.LinearLayoutInfo
import com.facebook.litho.widget.Recycler
import com.facebook.litho.widget.RecyclerBinder
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.PackageInfo


const val LIST_STATE = "infoListState"

class PackagesActivity : AppCompatActivity() {

    private lateinit var infoList : List<PackageInfo>
    private lateinit var componentContext : ComponentContext
    private lateinit var binder : RecyclerBinder

    var task : LoadPackagesTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        componentContext = ComponentContext(this)

        binder = RecyclerBinder(componentContext,
                LinearLayoutInfo(componentContext, OrientationHelper.VERTICAL, false))

        val recycler = Recycler
                .create(componentContext)
                .binder(binder)
                .itemDecoration(PackagesItemDecorator(this))
                .build()

        setContentView(LithoView.create(componentContext, recycler))

        infoList = savedInstanceState?.getParcelableArrayList(LIST_STATE) ?: ArrayList()

        addContents()

        if(infoList.isEmpty())
            loadPackagesList()
    }

    override fun onSaveInstanceState(outState: android.os.Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList(LIST_STATE, ArrayList(infoList))
    }

    private fun showPackages(items: List<PackageInfo>) {
        task = null
        infoList = items
        addContents()
    }

    private fun loadPackagesList() {
        task = LoadPackagesTask(packageManager, this::showPackages)
        task!!.execute()
    }

    private fun addContents() {
        if (infoList.isEmpty()) {
            binder.insertItemAt(0,
                    ProgressContainer.create(componentContext)
                            .sizeDip(48)
                            .colorRes(R.color.colorAccent)
                            .build())
        } else {
            if (binder.itemCount > 0) {
                binder.removeRangeAt(0, binder.itemCount)
            }

            for ((i, pkg) in infoList.withIndex()) {

                binder.insertItemAt(i,
                        ComponentInfo.create()
                                .component(Card.create(componentContext)
                                        .content(PackageItem.create(componentContext)
                                                .item(pkg)
                                                .build()
                                        )
                                        .build()
                                )
                                .build()
                )

            }
        }
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