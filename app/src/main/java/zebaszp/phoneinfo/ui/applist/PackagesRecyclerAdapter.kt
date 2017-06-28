package zebaszp.phoneinfo.ui.applist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import zebaszp.phoneinfo.databinding.ItemPackageBinding
import zebaszp.phoneinfo.domain.PackageInfo

class PackagesRecyclerAdapter(private val items : List<PackageInfo>) : RecyclerView.Adapter<PackagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPackageBinding.inflate(layoutInflater, parent, false)
        return PackagesViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PackagesViewHolder, position: Int) = holder.bind(items[position])
}