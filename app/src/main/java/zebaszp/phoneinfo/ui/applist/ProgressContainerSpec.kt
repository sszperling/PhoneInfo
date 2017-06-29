package zebaszp.phoneinfo.ui.applist

import android.support.annotation.ColorRes
import com.facebook.litho.Column
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Progress
import com.facebook.yoga.YogaAlign

@LayoutSpec
class ProgressContainerSpec {

    companion object {

        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(c: ComponentContext,
                           @Prop sizeDip: Int,
                           @Prop @ColorRes colorRes: Int) : ComponentLayout =
                Column.create(c)
                        .child(Progress.create(c)
                                .colorRes(colorRes)
                                .withLayout()
                                .widthDip(sizeDip)
                                .heightDip(sizeDip)
                                .alignSelf(YogaAlign.CENTER)
                        )
                        .build()
    }
}