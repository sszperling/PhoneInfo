package zebaszp.phoneinfo.ui.applist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import zebaszp.phoneinfo.R;
import zebaszp.phoneinfo.domain.PackageInfo;


public class PackagesActivity extends AppCompatActivity {
	
	private static final String LIST_STATE = "infoListState";
	
	private ProgressBar loading;
	private RecyclerView recycler;
	private List<PackageInfo> infoList;
	
	private LoadPackagesTask task = null;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_packages);
		
		loading = findViewById(R.id.packages_loading);
		recycler = findViewById(R.id.packages_list);
		
		if(savedInstanceState != null) {
			infoList = savedInstanceState.getParcelableArrayList(LIST_STATE);
		}
		if(infoList == null) {
			infoList = new ArrayList<>();
		}
		
		if(!infoList.isEmpty()) {
			showPackages();
		} else {
			loadPackagesList();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(task == null) {
			outState.putParcelableArrayList(LIST_STATE, new ArrayList<>(infoList));
		}
	}
	
	private void showPackages() {
		task = null;
		loading.setVisibility(View.GONE);
		recycler.setVisibility(View.VISIBLE);
		recycler.setAdapter(new PackagesRecyclerAdapter(infoList));
	}
	
	@UiThread
	private void loadPackagesList() {
		loading.setVisibility(View.VISIBLE);
		recycler.setVisibility(View.GONE);
		
		task = new LoadPackagesTask(getPackageManager(),
				it -> {
			infoList = it;
			showPackages();
		});
		task.execute();
	}
}
