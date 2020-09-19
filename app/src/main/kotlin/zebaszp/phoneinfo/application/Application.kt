package zebaszp.phoneinfo.application

import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        Stetho.initializeWithDefaults(this)
    }
}