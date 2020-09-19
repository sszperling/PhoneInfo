package zebaszp.phoneinfo.ui.applist

import androidx.annotation.ColorRes
import com.facebook.litho.Column
import com.facebook.litho.Component
import com.facebook.litho.ComponentContext
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Progress
import com.facebook.yoga.YogaAlign

@LayoutSpec
object ProgressContainerSpec {
    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext,
                       @Prop sizeDip: Float,
                       @Prop @ColorRes colorRes: Int) : Component =
            Column.create(c)
                    .child(Progress.create(c)
                            .colorRes(colorRes)
                            .widthDip(sizeDip)
                            .heightDip(sizeDip)
                            .alignSelf(YogaAlign.CENTER)
                    )
                    .build()
}