package zebaszp.phoneinfo.ui.main

import android.content.Intent
import com.facebook.litho.*
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.OnEvent
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge
import zebaszp.phoneinfo.R
import zebaszp.phoneinfo.domain.DeviceInfo
import zebaszp.phoneinfo.ui.applist.PackagesActivity

@LayoutSpec
class MainComponentSpec {

    companion object {

        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(c: ComponentContext, @Prop info: DeviceInfo) : ComponentLayout =
                Column.create(c)
                        .paddingDip(YogaEdge.ALL, 16)
                        .child(Row.create(c)
                                .child(Column.create(c)
                                        .child(Text.create(c)
                                              .textRes(R.string.deviceLabel)
                                               .textSizeSp(14f))
                                       .child(Text.create(c)
                                                .textRes(R.string.yearClassLabel)
                                                .textSizeSp(14f))
                                        .child(Text.create(c)
                                                .textRes(R.string.densityLabel)
                                                .textSizeSp(14f))
                                        .build()
                                )
                                .child(Column.create(c)
                                        .marginDip(YogaEdge.START, 16)
                                        .child(Text.create(c)
                                                .text(android.os.Build.MODEL)
                                                .textSizeSp(14f))
                                        .child(Text.create(c)
                                                .text(info.yearClass.toString())
                                                .textSizeSp(14f))
                                        .child(Text.create(c)
                                                .text(info.density)
                                                .textSizeSp(14f))
                                        .build()
                               )
                                .flexGrow(1f)
                                .build()
                        )
                        .child(
                                Button.create(c)
                                        .textRes(R.string.packagesButtonLabel)
                                        .textSizeSp(18f)
                                        .clickHandler(MainComponent.onClickButton(c)))
                        .build()

        @JvmStatic
        @OnEvent(ClickEvent::class)
        fun onClickButton(c: ComponentContext) {
            val intent = Intent(c, PackagesActivity::class.java)
            c.startActivity(intent)
        }
    }
}