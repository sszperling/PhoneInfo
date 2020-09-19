package zebaszp.phoneinfo.ui.applist

import com.facebook.litho.Column
import com.facebook.litho.Component
import com.facebook.litho.ComponentContext
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.annotations.ResType
import com.facebook.litho.widget.Progress
import com.facebook.yoga.YogaAlign

@LayoutSpec
object ProgressContainerSpec {
    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext,
                       @Prop(resType = ResType.DIMEN_SIZE) size: Int,
                       @Prop(resType = ResType.COLOR) color: Int) : Component =
            Column.create(c)
                    .child(Progress.create(c)
                            .color(color)
                            .widthPx(size)
                            .heightPx(size)
                            .alignSelf(YogaAlign.CENTER)
                    )
                    .build()
}