package zebaszp.phoneinfo.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.device.yearclass.YearClass
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.ui.applist.PackagesActivity


class MainActivity : AppCompatActivity() {

    companion object {
        val otherDensities = intArrayOf(260, 280, 360, 400, 420, 560)
    }

    private lateinit var modelText : TextView
    private lateinit var yearClassText : TextView
    private lateinit var densityText : TextView
    private lateinit var packagesButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        modelText = findViewById(R.id.info_model)
        yearClassText = findViewById(R.id.info_yearclass)
        densityText = findViewById(R.id.info_density)
        packagesButton = findViewById(R.id.button_packages)

        modelText.text = android.os.Build.MODEL
        yearClassText.text = YearClass.get(applicationContext).toString()
        densityText.text = getString(getDensityString(), resources.displayMetrics.densityDpi)

        packagesButton.setOnClickListener({v -> onClickPackages(v)})
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

    fun onClickPackages(view: View) {
        val intent = Intent(view.context, PackagesActivity::class.java)
        view.context.startActivity(intent)
    }
}