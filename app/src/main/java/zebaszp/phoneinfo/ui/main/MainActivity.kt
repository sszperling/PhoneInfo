package zebaszp.phoneinfo.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import com.facebook.device.yearclass.YearClass
import com.facebook.litho.ComponentContext
import com.facebook.litho.LithoView
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.DeviceInfo


class MainActivity : AppCompatActivity() {

    companion object {
        val otherDensities = intArrayOf(260, 280, 360, 400, 420, 560)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val info = DeviceInfo(YearClass.get(applicationContext), getString(getDensityString(), resources.displayMetrics.densityDpi))

        val componentContext = ComponentContext(this)

        val component = MainComponent.create(componentContext).info(info).build()

        setContentView(LithoView.create(componentContext, component))
    }

    fun getDensityString() : Int = when (resources.displayMetrics.densityDpi) {
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