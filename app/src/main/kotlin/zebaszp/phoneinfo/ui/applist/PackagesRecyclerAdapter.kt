package zebaszp.phoneinfo.ui.applist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.PackageInfo

class PackagesRecyclerAdapter(private val items : List<PackageInfo>) : RecyclerView.Adapter<PackagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_package, parent, false)
        return PackagesViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PackagesViewHolder, position: Int) {

        holder.appName.text =
                holder.itemView.context.getString(R.string.packageItemAppName, items[position].name)
        holder.pkgName.text =
                holder.itemView.context.getString(R.string.packageItemPkgName, items[position].appInfo.packageName)
    }
}

class PackagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val appName : TextView = itemView.findViewById(R.id.package_item_appname)
    val pkgName : TextView = itemView.findViewById(R.id.package_item_pkgname)
}