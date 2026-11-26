# AUTO•TQSM V2 — Functional Source Audit 01

**Date:** 2026-11-26  
**Branch:** `auto-tqsm-v2-clean-start`  
**Scope:** Read-only inspection of current V2 source; no runtime claims.

## Files inspected

- `fermata/src/main/AndroidManifest.xml`
- `fermata/src/auto/AndroidManifest.xml`
- `modules/web/build.gradle`
- `modules/web/src/main/AndroidManifest.xml`
- `fermata/src/main/java/me/aap/fermata/ui/activity/MainActivity.java`

## Confirmed source facts

1. The main manifest declares the Google Play Services Android Auto application metadata and launcher activity. The launcher activity itself has MAIN/LAUNCHER and LEANBACK_LAUNCHER filters; this source does not show it as a standalone Android Auto browser/video activity.
2. The auto manifest declares Fermata's projection services, Mirror services, a Navigation CarAppService, projection activity/service, accessibility dispatcher, Xposed dispatcher, and a media-service starter receiver.
3. The auto manifest contains Xposed module metadata. This is a significant dependency/compatibility consideration and must be understood before deciding whether the existing projection/mirroring feature can be supported across Android 10–16 without external modules or special setup.
4. The auto manifest declares `android.hardware.type.automotive` as required in that manifest. The merged manifest for each build variant must be inspected before drawing conclusions about phone installability.
5. The web dynamic feature registers two add-ons: `YoutubeAddon` and `WebBrowserAddon`. It depends on the existing Fermata host, AndroidX Media, Play Feature Delivery, AppCompat, Material, ConstraintLayout, SwipeRefreshLayout and WebKit.
6. The web feature manifest is a dynamic feature split. It is install-time only on devices with the Leanback feature and otherwise on-demand/fused. Phone and Auto runtime installation/availability of the module must be tested; source registration alone does not prove that the add-ons are available on every target device.
7. `MainActivity.checkUpdates()` contains a hard-coded upstream release endpoint: `https://api.github.com/repos/AndreyPavlenko/Fermata/releases/latest`. Its updater downloads upstream Fermata APK assets based on ABI. This is a release-critical branding/product ownership issue: V2 must not silently offer to replace itself with an upstream Fermata APK.
8. The same activity calls update checking only for AUTO builds and selects `-arm.apk` or `-arm64.apk` based on supported ABI. The V2 release/update strategy must be deliberately changed and tested before release.

## Priority functional risks

### P0 — Update ownership
Do not ship a V2 release while its in-app updater still points at the upstream Fermata release endpoint. Choose one explicit strategy:
- disable the in-app updater for V2 until an official V2 update channel exists; or
- point it to a V2-controlled release endpoint and validate asset naming, signatures, version ordering and rollback behavior.

No update endpoint was changed in this audit.

### P0 — Android Auto feature path
Map which current user-facing Android Auto behavior relies on projection, Mirror, MediaSession, CarAppService, accessibility and/or Xposed. Do not remove or add manifest categories based on guesswork. Test actual connection and launch behavior on target head units.

### P1 — Android 10–16 compatibility
Audit all dependencies and API calls for minSdk/runtime constraints. Inspect merged manifests for mobile and Auto variants. Run Gradle builds and emulator/device tests across Android 10–16. A minSdk value is not proof of runtime compatibility.

### P1 — YouTube and Browser
Confirm the add-on classes, their playback/navigation paths, dynamic feature delivery, and their actual behavior on phone. Separately test the owner's required Android Auto display workflow. Do not claim that a phone-side add-on registration makes a corresponding Android Auto screen appear.

### P1 — Permissions and special access
Review storage, accessibility, overlay, media projection, Bluetooth and Xposed-related permissions/access. Remove nothing until dependencies are mapped; do not add special access requirements without owner approval and a clear technical need.

## Next engineering actions

1. Run the existing Gradle workflow/build and retrieve the exact build result and artifacts.
2. Inspect merged manifests for all variants and resolve any installability/API issues.
3. Trace the update call site and implement the selected V2 update policy.
4. Trace media service and Android Auto projection startup/entry points.
5. Trace YouTube/Browser dynamic-feature installation and runtime registration.
6. Build a test matrix for Android 10–16 and the actual head unit.
7. Only then begin selective component integration from AA Browser/FermataX.

## Status

- Read-only source inspection completed for the listed files.
- No application source was changed in this audit.
- No APK build or device test was performed in this audit.
- The findings above are source-level observations, not proof of runtime behavior.
