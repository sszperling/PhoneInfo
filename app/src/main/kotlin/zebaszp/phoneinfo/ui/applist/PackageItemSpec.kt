package zebaszp.phoneinfo.ui.applist

import com.facebook.litho.Column
import com.facebook.litho.Component
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Card
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.PackageInfo

@LayoutSpec
object PackageItemSpec {
    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext, @Prop item: PackageInfo) : Component =
        Card.create(c).content(
            Column.create(c)
                .marginDip(YogaEdge.ALL, 16f)
                .child(Text.create(c)
                    .textRes(R.string.packageItemAppName, item.name)
                    .textSizeSp(14f))
                .child(Text.create(c)
                    .textRes(R.string.packageItemPkgName, item.packageName)
                    .textSizeSp(14f)))
            .build()
}