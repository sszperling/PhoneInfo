package zebaszp.phoneinfo.domain

import android.content.pm.ApplicationInfo
import android.os.Parcel
import android.os.Parcelable

class PackageInfo(val name: String, val appInfo: ApplicationInfo) : Parcelable {

    constructor(source: Parcel) : this(source.readString(),
            source.readParcelable(ApplicationInfo::class.java.classLoader))

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<PackageInfo> = object : Parcelable.Creator<PackageInfo> {

            override fun createFromParcel(source: Parcel): PackageInfo = PackageInfo(source)

            override fun newArray(size: Int): Array<PackageInfo?> = arrayOfNulls(size)
        }
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(name)
        out.writeParcelable(appInfo, flags)
    }

    override fun describeContents(): Int = 0
}