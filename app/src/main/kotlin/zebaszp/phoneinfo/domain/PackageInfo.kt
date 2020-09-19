package zebaszp.phoneinfo.domain

import android.content.pm.ApplicationInfo
import android.os.Parcel
import android.os.Parcelable

class PackageInfo(val name: String, val appInfo: ApplicationInfo) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmStatic
        val CREATOR: Parcelable.Creator<PackageInfo> = object : Parcelable.Creator<PackageInfo> {

            override fun createFromParcel(source: Parcel): PackageInfo = PackageInfo(source.readString()!!, source.readParcelable()!!)

            override fun newArray(size: Int): Array<PackageInfo?> = arrayOfNulls(size)
        }

        private inline fun <reified T : Parcelable> Parcel.readParcelable(): T? = readParcelable(T::class.java.classLoader)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(name)
        out.writeParcelable(appInfo, flags)
    }

    override fun describeContents(): Int = 0
}