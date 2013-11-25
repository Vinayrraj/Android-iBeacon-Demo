package com.radiusnetworks.ibeaconreference;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;

public class BeaconServiceUtility {

	private Context context;
	private PendingIntent pintent;
	private AlarmManager alarm;
	private Intent iService;

	public BeaconServiceUtility(Context context) {
		super();
		this.context = context;
		iService = new Intent(context, BeaconDetactorService.class);
		alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		pintent = PendingIntent.getService(context, 0, iService, 0);
	}

	public void onStart(IBeaconManager iBeaconManager, IBeaconConsumer consumer) {

		stopBackgroundScan();
		iBeaconManager.bind(consumer);

	}

	public void onStop(IBeaconManager iBeaconManager, IBeaconConsumer consumer) {

		iBeaconManager.unBind(consumer);
		startBackgroundScan();

	}

	private void stopBackgroundScan() {

		alarm.cancel(pintent);
		context.stopService(iService);
	}

	private void startBackgroundScan() {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 2);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 360000, pintent); // 6*60 * 1000
		context.startService(iService);
	}

}
