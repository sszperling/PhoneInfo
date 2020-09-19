package zebaszp.phoneinfo.ui.main

import android.text.Layout
import androidx.annotation.StringRes
import com.facebook.litho.*
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Text

@LayoutSpec
object ButtonSpec {
    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext,
                       @Prop @StringRes textRes: Int,
                       @Prop textSizeSp : Float,
                       @Prop onClickHandler: EventHandler<ClickEvent>): Component =
            Text.create(c)
                    .textRes(textRes)
                    .textSizeSp(textSizeSp)
                    .textAlignment(Layout.Alignment.ALIGN_CENTER)
                    .clickHandler(onClickHandler)
                    .build()

}