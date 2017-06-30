package zebaszp.phoneinfo.domain;

import android.content.pm.ApplicationInfo;

import zebaszp.annotations.ParcelableSpec;

@ParcelableSpec
public class PackageInfoSpec {
	
	private final String name;
	private final ApplicationInfo appInfo;
	
	public PackageInfoSpec(String name, ApplicationInfo appInfo) {
		this.name = name;
		this.appInfo = appInfo;
	}
}
