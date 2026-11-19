package me.aap.fermata.auto;

import static android.os.SystemClock.uptimeMillis;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.obtain;
import static me.aap.fermata.auto.AccessibilityEventDispatcherService.getWindowCount;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.aap.fermata.FermataApplication;
import me.aap.fermata.ui.activity.MainActivity;

abstract class EventDispatcher {

	static EventDispatcher get() {
		if (getWindowCount() > 1) return AccessibilityDispatcher.get();
		var d = ActivityDispatcher.get();
		return (d.getActiveInstance() == null) ? AccessibilityDispatcher.get() : d;
	}

	public abstract boolean back();

	public abstract boolean home();

	public abstract boolean tap(float x, float y);

	public abstract boolean motionEvent(MotionEvent e);

	public abstract boolean motionEvent(long downTime, long eventTime, int action, float x, float y);

	public boolean scale(float x, float y, float diff) {
		return AccessibilityDispatcher.get().scale(x, y, diff);
	}

	@Nullable
	AppCompatActivity getActivity() {
		return null;
	}


	private static final class ActivityDispatcher extends EventDispatcher {
		private static final ActivityDispatcher instance = new ActivityDispatcher();
		private InputMethodManager imm;

		@NonNull
		static ActivityDispatcher get() {
			return instance;
		}

		@Override
		public boolean back() {
			var a = getActivity();
			if (a == null) return AccessibilityDispatcher.get().back();
			//noinspection deprecation
			a.onBackPressed();
			return true;
		}

		@Override
		public boolean home() {
			return AccessibilityDispatcher.get().home();
		}

		@Override
		public boolean tap(float x, float y) {
			AppCompatActivity a = getActivity();
			if (a == null) return AccessibilityDispatcher.get().tap(x, y);
			var time = uptimeMillis();
			var e = obtain(time, time, ACTION_DOWN, x, y, 0);
			a.dispatchTouchEvent(e);
			e.recycle();
			e = obtain(time, time + 10, ACTION_UP, x, y, 0);
			a.dispatchTouchEvent(e);
			e.recycle();
			return true;
		}

		@Override
		public boolean motionEvent(MotionEvent e) {
			var a = getActivity();
			if (a == null) return AccessibilityDispatcher.get().motionEvent(e);
			a.dispatchTouchEvent(e);
			return true;
		}

		@Override
		public boolean motionEvent(long downTime, long eventTime, int action, float x, float y) {
			var a = getActivity();
			if (a == null)
				return AccessibilityDispatcher.get().motionEvent(downTime, eventTime, action, x, y);
			var e = obtain(downTime, eventTime, action, x, y, 0);
			a.dispatchTouchEvent(e);
			e.recycle();
			return true;
		}

		@Override
		AppCompatActivity getActivity() {
			var cnt = getWindowCount();
			if (cnt == -1) {
				if (imm == null) {
					imm = (InputMethodManager) FermataApplication.get()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
				}
				if (imm.isAcceptingText()) return null;
			} else if (cnt > 1) {
				return null;
			}
			return getActiveInstance();
		}

		private AppCompatActivity getActiveInstance() {
			AppCompatActivity a = LauncherActivity.getActiveInstance();
			if (a == null) a = MainActivity.getActiveInstance();
			return (a == null) || ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) &&
					(a.isInMultiWindowMode() || a.isInPictureInPictureMode())) ? null : a;
		}
	}

	private static final class AccessibilityDispatcher extends EventDispatcher {
		private static final AccessibilityDispatcher instance = new AccessibilityDispatcher();

		@NonNull
		static AccessibilityDispatcher get() {
			return instance;
		}

		@Override
		public boolean back() {
			return AccessibilityEventDispatcherService.dispatchBack();
		}

		@Override
		public boolean home() {
			return AccessibilityEventDispatcherService.dispatchHome();
		}

		@Override
		public boolean tap(float x, float y) {
			return AccessibilityEventDispatcherService.dispatchTap(x, y);
		}

		@Override
		public boolean scale(float x, float y, float diff) {
			return AccessibilityEventDispatcherService.dispatchScale(x, y, diff);
		}

		@Override
		public boolean motionEvent(MotionEvent e) {
			return AccessibilityEventDispatcherService.dispatchMotionEvent(e);
		}

		@Override
		public boolean motionEvent(long downTime, long eventTime, int action, float x, float y) {
			return AccessibilityEventDispatcherService.dispatchMotionEvent(downTime, eventTime, action, x, y);
		}
	}

}
