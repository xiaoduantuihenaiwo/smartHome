package Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smarthome.R;

public class InfoShower {
	
	public static void showInfo(String string, Context context) {
		Toast toast = Toast.makeText(context, string, 0);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastLayout = (LinearLayout)toast.getView();
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.ic_launcher);
		toastLayout.addView(imageView,0);
		toast.show();	
	}

}
