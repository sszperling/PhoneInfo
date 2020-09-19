package zebaszp.phoneinfo.ui.main

import android.graphics.Color
import android.text.Layout
import com.facebook.litho.*
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.annotations.ResType
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge

@LayoutSpec
object ButtonSpec {
    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext,
                       @Prop(resType = ResType.STRING) text: String,
                       @Prop(resType = ResType.DIMEN_TEXT) textSize : Int,
                       @Prop onClickHandler: EventHandler<ClickEvent>): Component =
            Text.create(c)
                    .backgroundColor(Color.LTGRAY)
                    .paddingDip(YogaEdge.ALL, 8f)
                    .text(text)
                    .textSizePx(textSize)
                    .textAlignment(Layout.Alignment.ALIGN_CENTER)
                    .clickHandler(onClickHandler)
                    .build()

}