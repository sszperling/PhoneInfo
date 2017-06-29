package zebaszp.phoneinfo.domain;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class PackageInfo implements Parcelable {
	
	private final String name;
	private final ApplicationInfo appInfo;
	
	public static final Parcelable.Creator<PackageInfo> CREATOR = new Parcelable.Creator<PackageInfo>() {
		
		@Override
		public PackageInfo createFromParcel(Parcel parcel) {
			return new PackageInfo(parcel);
		}
		
		@Override
		public PackageInfo[] newArray(int size) {
			return new PackageInfo[size];
		}
	};
	
	public PackageInfo(String name, ApplicationInfo appInfo) {
		this.name = name;
		this.appInfo = appInfo;
	}
	
	PackageInfo(Parcel source) {
		this.name = source.readString();
		this.appInfo = source.readParcelable(ApplicationInfo.class.getClassLoader());
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeParcelable(appInfo, flags);
	}
	
	public String getName() {
		return name;
	}
	
	public ApplicationInfo getAppInfo() {
		return appInfo;
	}
}
