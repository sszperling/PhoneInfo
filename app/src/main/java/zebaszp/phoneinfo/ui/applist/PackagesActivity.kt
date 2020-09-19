package zebaszp.phoneinfo.ui.applist

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.OrientationHelper
import com.facebook.litho.ComponentContext
import com.facebook.litho.LithoView
import com.facebook.litho.widget.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.PackageInfo


const val LIST_STATE = "infoListState"

class PackagesActivity : AppCompatActivity() {

    private lateinit var infoList : List<PackageInfo>
    private lateinit var componentContext : ComponentContext
    private lateinit var binder : RecyclerBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        componentContext = ComponentContext(this)

        binder = RecyclerBinder.Builder()
                .layoutInfo(LinearLayoutInfo(componentContext, OrientationHelper.VERTICAL, false))
                .build(componentContext)

        val recycler = Recycler
                .create(componentContext)
                .binder(binder)
                .itemDecoration(PackagesItemDecorator(this))
                .build()

        setContentView(LithoView.create(componentContext, recycler))

        infoList = listOf()
        // this state might be too large, so we can't persist it
        // infoList = savedInstanceState?.getParcelableArrayList(LIST_STATE) ?: listOf()

        addContents()

        if(infoList.isEmpty())
            loadPackagesList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // this state might be too large, so we can't persist it
        // outState.putParcelableArrayList(LIST_STATE, ArrayList(infoList))
    }

    private fun showPackages(items: List<PackageInfo>) {
        infoList = items
        addContents()
    }

    private fun loadPackagesList() {
        lifecycleScope.launchWhenResumed {
            showPackages(withContext(Dispatchers.IO) {
                val items = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                items.map {
                    PackageInfo(packageManager.getApplicationLabel(it).toString(), it)
                }
            })
        }
    }

    private fun addContents() {
        if (infoList.isEmpty()) {
            binder.insertItemAt(0,
                    ProgressContainer.create(componentContext)
                            .sizeDip(48f)
                            .colorRes(R.color.colorAccent)
                            .build())
        } else {
            if (binder.itemCount > 0) {
                binder.removeRangeAt(0, binder.itemCount)
            }

            for ((i, pkg) in infoList.withIndex()) {

                binder.insertItemAt(i,
                        ComponentRenderInfo.create()
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
