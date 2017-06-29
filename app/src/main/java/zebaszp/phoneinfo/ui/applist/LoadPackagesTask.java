package zebaszp.phoneinfo.ui.applist;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;

import zebaszp.phoneinfo.domain.PackageInfo;

public class LoadPackagesTask extends AsyncTask<Void, Void, List<PackageInfo>> {
	
	private final PackageManager pm;
	private final LoadPackagesCallback callback;
	
	LoadPackagesTask(PackageManager pm, LoadPackagesCallback callback) {
		this.pm = pm;
		this.callback = callback;
	}
	
	@Override
	protected List<PackageInfo> doInBackground(Void... voids) {
		List<ApplicationInfo> items = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		List<PackageInfo> results = new LinkedList<>();
		for (ApplicationInfo item : items) {
			results.add(new PackageInfo(pm.getApplicationLabel(item).toString(), item));
		}
		return results;
	}
	
	@Override
	protected void onPostExecute(List<PackageInfo> results) {
		callback.onPackagesLoaded(results);
	}
	
	public interface LoadPackagesCallback {
		void onPackagesLoaded(List<PackageInfo> results);
	}
}
