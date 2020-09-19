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
object MainComponentSpec {

    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext, @Prop info: DeviceInfo) : Component =
            Column.create(c)
                    .paddingDip(YogaEdge.ALL, 16f)
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
                            )
                            .child(Column.create(c)
                                    .marginDip(YogaEdge.START, 16f)
                                    .child(Text.create(c)
                                            .text(android.os.Build.MODEL)
                                            .textSizeSp(14f))
                                    .child(Text.create(c)
                                            .text(info.yearClass.toString())
                                            .textSizeSp(14f))
                                    .child(Text.create(c)
                                            .text(info.density)
                                            .textSizeSp(14f))
                           )
                            .flexGrow(1f)
                    )
                    .child(Button.create(c)
                            .textRes(R.string.packagesButtonLabel)
                            .textSizeSp(18f)
                            .onClickHandler(MainComponent.onClickButton(c)))
                    .build()

    @OnEvent(ClickEvent::class)
    fun onClickButton(c: ComponentContext) {
        val intent = Intent(c.androidContext, PackagesActivity::class.java)
        c.androidContext.startActivity(intent)
    }
}