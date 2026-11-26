# AUTO•TQSM V2 — YouTube, Browser and Android Auto Path Audit 03

Date: 2026-11-26
Branch: `auto-tqsm-v2-clean-start`
Scope: read-only source tracing after the phone-installability manifest correction.

## 1. Android Auto entry path

Inspected:
- `fermata/src/auto/AndroidManifest.xml`
- `fermata/src/auto/java/me/aap/fermata/auto/CarService.java`
- `fermata/src/auto/java/me/aap/fermata/auto/MainCarActivity.java`

Findings:
- The Auto flavor declares `CarService` with Android Auto projection category intent filters.
- `CarService.getCarActivity()` returns `MainCarActivity.class`.
- `MainCarActivity` connects to `FermataMediaServiceConnection` and constructs the same `MainActivityDelegate` used by the phone UI.
- This is an app-projection activity path, not simply an Android Auto media-browser list. The Android Auto screen is hosted through the Auto SDK `CarActivity` and shared application delegate.
- The existing media session remains a separate audio/control integration. Do not add fake media-browser categories to expose the browser or video UI.

## 2. YouTube path

Inspected:
- `modules/web/build.gradle`
- `modules/web/src/main/java/me/aap/fermata/addon/web/yt/YoutubeAddon.java`
- `modules/web/src/main/java/me/aap/fermata/addon/web/yt/YoutubeFragment.java`
- `modules/web/src/main/java/me/aap/fermata/addon/web/yt/YoutubeMediaEngine.java`

Findings:
- YouTube is an add-on in the `web` dynamic feature module.
- `YoutubeAddon` extends `WebBrowserAddon`, and `YoutubeFragment` uses the WebView-based YouTube page.
- `YoutubeMediaEngine` bridges YouTube playback state and controls to Fermata's `MediaSessionCallback`.
- In the Auto build, the source has a preference to attempt ad skipping and options to resume the last video; this is existing upstream behavior, not a V2 test result.
- `YoutubeFragment` deliberately reopens the last video paused when the car screen starts, so it does not automatically start video playback merely because the car screen opened.
- This source trace confirms the implementation route exists; it does not prove playback works on the owner's head unit or current YouTube website.

## 3. Browser path

Inspected:
- `modules/web/src/main/java/me/aap/fermata/addon/web/WebBrowserAddon.java`
- `modules/web/src/main/java/me/aap/fermata/addon/web/WebBrowserFragment.java`
- `modules/web/src/main/AndroidManifest.xml`
- `modules/web/build.gradle`

Findings:
- Browser is a WebView add-on in the same `web` dynamic feature module as YouTube.
- It stores the last URL and bookmarks in its preference store.
- The module manifest declares on-demand delivery and install-time delivery only for Leanback devices, with fusing enabled.
- `AddonManager` loads an add-on when its class is available; if the module is absent, its install path requests the dynamic module. The default enabled state is based on whether the add-on class is installed.
- The CI workflow builds a universal APK for each ABI. Whether the feature module is fused into the produced universal APK must be confirmed from a successful build and APK inspection; do not assume it from source alone.

## 4. Build/installability correction already applied

The Auto-flavor manifest previously required `android.hardware.type.automotive`. This identifies Android Automotive OS hardware and is not the target device for an Android phone running Android Auto projection.
- Changed it to `android:required="false"` in `fermata/src/auto/AndroidManifest.xml`.
- This preserves the Auto services and their intent filters.
- Commit: `b2476d490ab1b0447027ee642804ac4f3fb3c2ce`.

## 5. Updater safety correction already applied

The inherited updater endpoint queried upstream Fermata releases. It is disabled in V2 until a V2-owned update channel and signing policy exist.
- Commit: `86b050f056042264177b07779ca65a51cdb6c8fe`.

## 6. Current build verification

A GitHub Actions run was automatically started by the latest push:
- Run: `45765024302`
- Commit: `e622c5c27ca39f08359c1ade1661ded436f8bf28`
- Status at audit time: in progress; no success/failure conclusion yet.
- Run URL: https://github.com/androidautoplayer-mie/Fermata/actions/runs/45765024302

## 7. Not yet verified / next engineering checks

1. Wait for CI result and inspect build logs if it fails.
2. Inspect the generated universal APK contents and merged manifest: confirm the `web` feature, YouTube and Browser classes/resources are included and phone installability is preserved.
3. Install on an Android phone; confirm first launch and both add-ons appear/load without requesting unrelated permissions.
4. Connect to a real Android Auto head unit; confirm CarService launches the Auto activity and the shared delegate renders correctly.
5. Test YouTube page load, video selection, pause/resume, session controls and leaving/re-entering the car screen.
6. Test Browser page load, navigation, bookmarks and session persistence.
7. Test day/night transition from vehicle lighting signal separately; this audit does not establish that it is implemented correctly.
8. Keep Accessibility and Xposed isolated to Mirror-related paths until their necessity is demonstrated. Do not remove or require them as part of this audit.

No YouTube, Browser, MediaSession, or visual UI code was modified in this audit. No APK has yet been device-tested.
