package zebaszp.phoneinfo.domain

import android.os.Parcel
import android.os.Parcelable

class PackageInfo(val name: String, val packageName: String) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<PackageInfo> = object : Parcelable.Creator<PackageInfo> {

            override fun createFromParcel(source: Parcel): PackageInfo = PackageInfo(source.readString()!!, source.readString()!!)

            override fun newArray(size: Int): Array<PackageInfo?> = arrayOfNulls(size)
        }
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(name)
        out.writeString(packageName)
    }

    override fun describeContents(): Int = 0
}