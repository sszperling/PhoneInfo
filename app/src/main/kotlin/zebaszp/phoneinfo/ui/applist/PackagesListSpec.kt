package zebaszp.phoneinfo.ui.applist

import com.facebook.litho.StateValue
import com.facebook.litho.annotations.*
import com.facebook.litho.sections.Children
import com.facebook.litho.sections.LoadingEvent
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.SectionLifecycle
import com.facebook.litho.sections.annotations.*
import com.facebook.litho.sections.common.DataDiffSection
import com.facebook.litho.sections.common.RenderEvent
import com.facebook.litho.widget.ComponentRenderInfo
import com.facebook.litho.widget.RenderInfo
import zebaszp.phoneinfo.domain.PackageInfo


@GroupSectionSpec
object PackagesListSpec {

    @Suppress("UNUSED_PARAMETER")
    @OnCreateInitialState
    fun onCreateInitialState(c: SectionContext, packages: StateValue<List<PackageInfo>>, computing: StateValue<Boolean>) {
        packages.set(listOf())
        computing.set(true)
    }

    @OnCreateChildren
    fun onCreateChildren(c: SectionContext, @State packages: List<PackageInfo>): Children = Children.create()
        .child(DataDiffSection.create<PackageInfo>(SectionContext(c))
            .data(packages)
            .renderEventHandler(PackagesList.onRender(c))
        )
        .build()

    @OnEvent(RenderEvent::class)
    fun onRender(c: SectionContext, @FromEvent model: PackageInfo): RenderInfo = ComponentRenderInfo.create()
            .component(PackageItem.create(c).item(model))
            .build()

    @Suppress("UNUSED_PARAMETER")
    @OnCreateService
    fun onCreateService(c: SectionContext) = PackageInfoLoader()

    @OnBindService
    fun onBindService(c: SectionContext, service: PackageInfoLoader, @State computing: Boolean) {
        if (computing) service.getPackages(c.androidContext, PackagesList.onDataLoaded(c))
    }

    @OnEvent(PackagesModel::class)
    fun onDataLoaded(c: SectionContext, @FromEvent packages: List<PackageInfo>) {
        PackagesList.updatePackages(c, packages)
        PackagesList.markDone(c)
        SectionLifecycle.dispatchLoadingEvent(c, false, LoadingEvent.LoadingState.SUCCEEDED, null)
    }

    @OnUpdateState
    fun markDone(computing: StateValue<Boolean>) {
        computing.set(false)
    }

    @OnUpdateState
    fun updatePackages(packages: StateValue<List<PackageInfo>>, @Param newPackages: List<PackageInfo>) {
        packages.set(newPackages)
    }
}