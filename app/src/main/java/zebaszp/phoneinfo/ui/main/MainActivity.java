package zebaszp.phoneinfo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.device.yearclass.YearClass;

import zebaszp.phoneinfo.R;
import zebaszp.phoneinfo.ui.applist.PackagesActivity;

public class MainActivity extends AppCompatActivity {
	
	private static final int[] otherDensities = new int[]{260, 280, 360, 400, 420, 560};
	
	private TextView modelText;
	private TextView yearClassText;
	private TextView densityText;
	private Button packagesButton;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		modelText = findViewById(R.id.info_model);
		yearClassText = findViewById(R.id.info_yearclass);
		densityText = findViewById(R.id.info_density);
		packagesButton = findViewById(R.id.button_packages);
		
		modelText.setText(android.os.Build.MODEL);
		yearClassText.setText(String.valueOf(YearClass.get(getApplicationContext())));
		densityText.setText(getString(getDensityString(), getResources().getDisplayMetrics().densityDpi));
		
		packagesButton.setOnClickListener(this::onClickPackages);
	}
	
	private int getDensityString() {
		int dpi = getResources().getDisplayMetrics().densityDpi;
		switch (dpi) {
			case DisplayMetrics.DENSITY_LOW:
				return R.string.densityLow;
			case DisplayMetrics.DENSITY_MEDIUM:
				return R.string.densityMed;
			case DisplayMetrics.DENSITY_HIGH:
				return R.string.densityHi;
			case DisplayMetrics.DENSITY_XHIGH:
				return R.string.densityXHi;
			case DisplayMetrics.DENSITY_XXHIGH:
				return R.string.densityXXHi;
			case DisplayMetrics.DENSITY_XXXHIGH:
				return R.string.densityXXXHi;
			default:
				for (int density : otherDensities) {
					if(density == dpi)
						return R.string.densityKnown;
				}
				return R.string.densityUnknown;
		}
	}
	
	private void onClickPackages(@NonNull View view) {
		Intent intent = new Intent(this, PackagesActivity.class);
		startActivity(intent);
	}
}
