package me.aap.fermata.auto;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static me.aap.utils.async.Completed.completedVoid;
import static me.aap.utils.function.ResultConsumer.Cancel.isCancellation;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.provider.Settings;

import me.aap.fermata.FermataApplication;
import me.aap.utils.async.FutureSupplier;
import me.aap.utils.async.Promise;
import me.aap.utils.log.Log;
import me.aap.utils.ui.activity.ActivityBase;
import me.aap.utils.ui.activity.ActivityDelegate;
import me.aap.utils.ui.activity.AppActivity;

public class ProjectionActivity extends ActivityBase {
	private static Promise<Intent> promise;
	private static boolean starting;

	static FutureSupplier<Intent> start() {
		if (promise != null) promise.cancel();
		var p = promise = new Promise<>();
		p.thenRun(() -> {
			if (promise != p) return;
			promise = null;
			starting = false;
		});
		try {
			var app = FermataApplication.get();
			var intent = new Intent(app, ProjectionActivity.class);
			intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
			app.startActivity(intent);
		} catch (Exception err) {
			Log.e(err, "Failed to start ProjectionActivity");
			p.completeExceptionally(err);
		}
		return p;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (promise != null) promise.cancel();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (starting) return;
		var p = promise;
		if (p == null) {
			finish();
			return;
		}
		starting = true;
		checkOverlayPermission().thenIgnoreResult(this::checkWriteSettingsPermission)
				.thenIgnoreResult(this::requestScreenCapturePermission).onCompletion((i, err) -> {
					if (promise != p) return;
					starting = false;
					finish();
					if (err != null) {
						if (!isCancellation(err)) Log.i(err, "Screen capture request failed");
					} else if (i == null) {
						Log.i("Screen capture request rejected");
						p.cancel();
					} else {
						p.complete(i);
					}
				});
	}

	private FutureSupplier<?> checkOverlayPermission() {
		if (Settings.canDrawOverlays(this)) return completedVoid();
		Log.i("Requesting ACTION_MANAGE_OVERLAY_PERMISSION permission");
		return startActivityForResult(() -> new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse("package:" + getPackageName())));
	}

	private FutureSupplier<?> checkWriteSettingsPermission() {
		if (Settings.System.canWrite(this)) return completedVoid();
		Log.i("Requesting ACTION_MANAGE_WRITE_SETTINGS permission");
		return startActivityForResult(() -> new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
				Uri.parse("package:" + getPackageName())));
	}

	private FutureSupplier<Intent> requestScreenCapturePermission() {
		Log.i("Requesting screen cast permission");
		var mm = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
		return startActivityForResult(mm::createScreenCaptureIntent);
	}

	@Override
	protected FutureSupplier<? extends ActivityDelegate> createDelegate(AppActivity a) {
		return NO_DELEGATE;
	}
}
