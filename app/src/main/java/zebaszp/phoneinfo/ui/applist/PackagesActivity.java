package zebaszp.phoneinfo.ui.applist;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zebaszp.phoneinfo.R;
import zebaszp.phoneinfo.domain.PackageInfo;


public class PackagesActivity extends AppCompatActivity {
	
	//private static final String LIST_STATE = "infoListState";
	
	private ProgressBar loading;
	private RecyclerView recycler;
	private List<PackageInfo> infoList;

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_packages);
		
		loading = findViewById(R.id.packages_loading);
		recycler = findViewById(R.id.packages_list);
		
		/*
		if(savedInstanceState != null) {
			infoList = savedInstanceState.getParcelableArrayList(LIST_STATE);
		} else {
		*/
		infoList = new ArrayList<>();
		//}
		
		if(!infoList.isEmpty()) {
			showPackages();
		} else {
			loadPackagesList();
		}
	}
	
	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putParcelableArrayList(LIST_STATE, new ArrayList<>(infoList));
	}

	@UiThread
	private void showPackages() {
		loading.setVisibility(View.GONE);
		recycler.setVisibility(View.VISIBLE);
		recycler.setAdapter(new PackagesRecyclerAdapter(infoList));
	}
	
	@UiThread
	private void loadPackagesList() {
		loading.setVisibility(View.VISIBLE);
		recycler.setVisibility(View.GONE);

		PackageManager pm = getPackageManager();
		executor.submit(() -> {
			List<ApplicationInfo> items = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			List<PackageInfo> results = new LinkedList<>();
			for (ApplicationInfo item : items) {
				results.add(new PackageInfo(pm.getApplicationLabel(item).toString(), item));
			}
			runOnUiThread(() -> {
				infoList = results;
				showPackages();
			});
		});
	}
}
