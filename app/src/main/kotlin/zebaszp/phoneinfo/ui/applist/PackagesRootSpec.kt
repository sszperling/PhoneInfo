package zebaszp.phoneinfo.ui.applist

import com.facebook.litho.Component
import com.facebook.litho.ComponentContext
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import zebaszp.phoneinfo.R


@LayoutSpec
object PackagesRootSpec {
    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext): Component = RecyclerCollectionComponent.create(c)
        .disablePTR(true)
        .section(PackagesList.create(SectionContext(c)))
        .loadingComponent(ProgressContainer.create(c)
            .sizeDip(48f)
            .colorRes(R.color.colorAccent))
        .build()
}