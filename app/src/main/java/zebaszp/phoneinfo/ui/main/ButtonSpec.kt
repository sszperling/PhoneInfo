package zebaszp.phoneinfo.ui.main

import android.text.Layout
import androidx.annotation.StringRes
import com.facebook.litho.ClickEvent
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.EventHandler
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Text

@LayoutSpec
class ButtonSpec {

    companion object {

        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(c: ComponentContext,
                           @Prop @StringRes textRes: Int,
                           @Prop textSizeSp : Float,
                           @Prop clickHandler: EventHandler<ClickEvent>): ComponentLayout =
                Text.create(c)
                        .textRes(textRes)
                        .textSizeSp(textSizeSp)
                        .textAlignment(Layout.Alignment.ALIGN_CENTER)
                        .withLayout()
                        .clickHandler(clickHandler)
                        .build()

    }
}