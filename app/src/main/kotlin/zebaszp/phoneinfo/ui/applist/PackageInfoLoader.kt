package zebaszp.phoneinfo.ui.applist

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.facebook.litho.EventHandler
import com.facebook.litho.annotations.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zebaszp.phoneinfo.domain.PackageInfo

class PackageInfoLoader {
    fun getPackages(context: Context, handler: EventHandler<PackagesModel>) = (context as AppCompatActivity).lifecycleScope
        .launchWhenResumed {
            val pm = context.packageManager
            handler.dispatchEvent(PackagesModel(
                withContext(Dispatchers.IO) {
                    val items = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                    items.map {
                        PackageInfo(pm.getApplicationLabel(it).toString(), it.packageName)
                    }
                }
            ))
        }
}

@Event
data class PackagesModel(@JvmField val packages: List<PackageInfo>)