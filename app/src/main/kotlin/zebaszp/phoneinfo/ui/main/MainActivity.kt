package zebaszp.phoneinfo.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import androidx.databinding.DataBindingUtil
import com.facebook.device.yearclass.YearClass
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.databinding.ActivityMainBinding
import zebaszp.phoneinfo.domain.DeviceInfo
import zebaszp.phoneinfo.ui.applist.PackagesActivity


class MainActivity : AppCompatActivity() {

    val otherDensities = intArrayOf(260, 280, 360, 400, 420, 560)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val info = DeviceInfo(YearClass.get(applicationContext), getString(getDensityString(), resources.displayMetrics.densityDpi))
        binding.info = info

        binding.handler = MainActivityHandler()
    }

    private fun getDensityString() : Int = when (resources.displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> R.string.densityLow
        DisplayMetrics.DENSITY_MEDIUM -> R.string.densityMed
        DisplayMetrics.DENSITY_HIGH -> R.string.densityHi
        DisplayMetrics.DENSITY_XHIGH -> R.string.densityXHi
        DisplayMetrics.DENSITY_XXHIGH -> R.string.densityXXHi
        DisplayMetrics.DENSITY_XXXHIGH -> R.string.densityXXXHi
        in otherDensities -> R.string.densityKnown
        else -> R.string.densityUnknown
    }
}

class MainActivityHandler {

    fun onClickPackages(view: View) {
        val intent = Intent(view.context, PackagesActivity::class.java)
        view.context.startActivity(intent)
    }
}
