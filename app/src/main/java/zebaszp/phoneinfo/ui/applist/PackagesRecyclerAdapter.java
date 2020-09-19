package zebaszp.phoneinfo.ui.applist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zebaszp.phoneinfo.R;
import zebaszp.phoneinfo.domain.PackageInfo;


class PackagesRecyclerAdapter extends RecyclerView.Adapter<PackagesRecyclerAdapter.PackagesViewHolder> {
	
	private final List<PackageInfo> items;
	
	PackagesRecyclerAdapter(List<PackageInfo> items) {
		this.items = items;
	}
	
	@Override
	public PackagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false);
		return new PackagesViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(PackagesViewHolder holder, int position) {
		holder.appName.setText(holder.itemView.getContext()
				.getString(R.string.packageItemAppName, items.get(position).getName()));
		holder.pkgName.setText(holder.itemView.getContext()
				.getString(R.string.packageItemPkgName, items.get(position).getAppInfo().packageName));
	}
	
	@Override
	public int getItemCount() {
		return items.size();
	}
	
	static class PackagesViewHolder extends RecyclerView.ViewHolder {
		
		final TextView appName;
		final TextView pkgName;
		
		PackagesViewHolder(View itemView) {
			super(itemView);
			appName = itemView.findViewById(R.id.package_item_appname);
			pkgName = itemView.findViewById(R.id.package_item_pkgname);
		}
	}
}
