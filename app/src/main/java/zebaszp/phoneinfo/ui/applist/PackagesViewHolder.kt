package zebaszp.phoneinfo.ui.applist

import android.support.v7.widget.RecyclerView
import zebaszp.phoneinfo.databinding.ItemPackageBinding
import zebaszp.phoneinfo.domain.PackageInfo


class PackagesViewHolder(val binding: ItemPackageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PackageInfo) {
        binding.item = item
        binding.executePendingBindings()
    }
}