# AUTO•TQSM — PROJECT MASTER / HANDOVER

Last updated: 2026-11-17 (UTC)
Repository: https://github.com/androidautoplayer-mie/Fermata
Working branch: `urgent24jam`
Project owner: user
Purpose: one AUTO•TQSM app targeting Android 10–17, RM0 cost, and Android Auto head-unit use; YouTube-first, with Browser subject to platform support and Mirror optional.

## 1. Non-negotiable scope

- App name shown by the build workflow: `AUTO•TQSM`.
- Deliver one APK/app package, not a collection of separate user-facing apps.
- Include exactly one YouTube addon and one Web Browser addon.
- Keep the existing Fermata host/framework where needed to host the addons; do not rebuild the whole application from scratch without a technical reason.
- Keep the YouTube "Try to Skip Ad" functionality already present in the YouTube addon, subject to its actual implementation and applicable platform rules.
- No Shizuku requirement.
- Do not require the user to enable Android Developer Options as a prerequisite.
- Budget target: RM0.
- Compatibility target: Android 10–17. Current source minSdk is 28 (Android 9+); Android 10–17 compatibility is a target, not yet verified across all OS versions.
- Must work with Android Auto players over USB and wireless where supported by the phone and head unit; both modes require real-device testing.
- Do not add IPTV, TV, Movie, Facebook, TikTok, screen mirroring, or MATAMIERA features to AUTO•TQSM. Those belong to separate future projects and require separate approval.
- Do not change the project scope, app identity, or user-approved design without explicit approval.
- Do not claim Android Auto compatibility merely because an APK compiles or installs on a phone. Verify the actual Android Auto manifest/category/service integration and test on a compatible head unit.
- Never facilitate video playback while driving or bypass Android Auto safety restrictions.

## 2. Current implementation facts (verified from branch source)

- `settings.gradle` selects only the `:web` dynamic-feature module.
- `modules/web/build.gradle` registers exactly two addons:
  - `YoutubeAddon` (`me.aap.fermata.addon.web.yt.YoutubeAddon`)
  - `WebBrowserAddon` (`me.aap.fermata.addon.web.WebBrowserAddon`)
- Root `build.gradle` has an empty base addon registry (`addons = []`); the selected `:web` module contributes the two addons above.
- The app remains based on the Fermata host/core. This is not a clean-room app rewrite.
- The build workflow sets `APP_NAME=AUTO•TQSM`, uses the `urgent24jam` package suffix, and builds arm64-v8a and armeabi-v7a universal APK outputs.
- AA Browser is a separate upstream codebase. Its relevant compatibility/launch code has not yet been integrated into this branch. Do not copy its entire app or manifest without a class-by-class and manifest merge review.

## 3. Repository and source control

- Repository: https://github.com/androidautoplayer-mie/Fermata
- Branch: `urgent24jam`
- Current HEAD before this documentation update: `c9e2777676dfdbf6857b67e40af045062a4d7526` (scope clarification only; no APK built from this HEAD).
- Previous successful baseline before recent registry cleanup: `4c6a0d5874687170d223ec5283e47773a52d154b`
- Commit `e3632ad41712e9590fdb7443fa0812b0fa9f00e9` removed the old base addon registry block but caused a build failure because Gradle expected the `ext.addons` property.
- Commit `2b0de5f4931579c7f3864f147769b5c85292c0fb` corrected that by explicitly setting `addons = []`. This is the current successful build commit.

## 4. Latest CI build evidence

- GitHub Actions run: https://github.com/androidautoplayer-mie/Fermata/actions/runs/44040206462
- Run ID: `44040206462`
- Commit: `2b0de5f4931579c7f3864f147769b5c85292c0fb`
- Result: **SUCCESS** (job and APK signature/package metadata verification steps succeeded).
- arm64 artifact ID: `14309775099`
- arm32 artifact ID: `14308529477`
- Artifact expiry reported by GitHub: 2026-12-17 UTC.
- This proves CI built and verified APK artifacts. It does **not** prove that the app works on the user's phone or is accepted/launched by Android Auto on the user's car player.

## 5. Known user-reported issues — unresolved until tested

1. Opening Settings on the phone caused the app to close/crash.
2. Tapping a YouTube video added it to a playlist rather than starting playback.
3. Play Protect showed a warning for sideloading. The user accepts that a sideloaded build may trigger a warning; do not describe the APK as harmless or recommend disabling device protection broadly.

Do not mark either Settings or YouTube playback as fixed without identifying the cause and verifying the corrected build. No crash log or runtime trace has been provided yet.

## 6. Android Auto integration status

- Fermata's main manifest contains Android Auto media-category metadata and the host's car-related integration.
- AA Browser's current codebase has a distinct launcher/navigation-oriented integration. The identical `automotive_app_desc.xml` file alone is not evidence that its launcher/browser integration has been merged.
- A source audit and safe manifest/component integration plan are still required.
- User's target is the Android Auto projection screen, not merely launching the APK on the phone.
- Do not state "Android Auto ready" until the actual category, services, exported components, feature delivery, app visibility, and head-unit behavior have been verified.
- The user's exact Android Auto/head-unit version and phone OS should be recorded when hardware testing becomes possible; do not guess them.

## 6A. First real Android Auto head-unit test (user-provided photos, 2026-11-17)

- The AUTO•TQSM launcher icon is visible in the Android Auto launcher on the user's OPPO A3 5G / Android 16.
- Opening the entry reaches a Fermata media-service style screen showing Folders, Favorites and Playlists, with "No items".
- This is evidence that Android Auto can discover the app/media entry and open the media browser surface. It is NOT evidence that the YouTube and Browser add-ons are exposed as Android Auto browse entries.
- Source inspection explains the empty root structure: `DefaultMediaLib.getChildren(Root)` returns last-played item (if any), Folders, Favorites, Playlists, and only addons implementing `MediaLibAddon`. The current `YoutubeAddon` extends `WebBrowserAddon` and does not implement `MediaLibAddon`; `WebBrowserAddon` is a phone-side fragment addon and also does not implement `MediaLibAddon`. Thus neither appears in the media-service browse root by design. The empty Folders/Favorites/Playlists are consistent with no media items/favorites/playlists configured; not proof of Android 16 incompatibility.
- Current test status: Android Auto launcher discovery = observed working; media-service root opens = observed working; YouTube/Browser usable from the Android Auto screen = NOT verified / not achieved in this test.
- Do not "fix" this by simply adding arbitrary media items or claiming Android 16 incompatibility. The architecture needs a deliberate, platform-compliant Android Auto entry/interaction path. Investigate Android Auto media-app capabilities and existing Fermata/AA Browser integration before code changes. Preserve safety restrictions; do not attempt to surface video/browser screens through unsupported hacks.

## 6B. Platform and AA Browser compatibility audit (2026-11-17)

- Official Android for Cars documentation distinguishes Android Auto from Android Automotive OS (AAOS). The supported category list says Android Auto supports audio media, while browser apps are listed for AAOS only; video apps are also AAOS-only. Reference: https://developer.android.com/training/cars
- Official media-app documentation says Android Auto media apps expose an audio-oriented content tree through MediaBrowserService/MediaLibraryService and MediaSession. This is not a general-purpose launcher for arbitrary phone activities or web browser UI. References:
  - https://developer.android.com/training/cars/media/create-media-browser
  - https://developer.android.com/training/cars/media/create-media-browser/content-hierarchy
- AA Browser current source (androidautohacks/AABrowser main, inspected 2026-11-17) is a standalone browser Activity/WebView app, not a MediaBrowserService addon. Its manifest registers MainActivity as MAIN/LAUNCHER, CAR_LAUNCHER, NAVIGATION and APP_MAPS and accepts http/https VIEW intents. The manifest also sets distractionOptimized=true. Do not copy these declarations into AUTO•TQSM as if they were official Android Auto browser support.
- AA Browser current app Gradle config sets minSdk 35, targetSdk 37, Java/Kotlin JVM target 21. This excludes Android 12 devices at the declared minimum and cannot simply be transplanted into this app's minSdk 28 build without a deliberate compatibility port.
- AA Browser README says Android 15 or later and its installation instructions require Android Auto Developer Settings / Unknown sources. This conflicts with the user's no-Developer-Options requirement. Do not integrate a workaround that requires that setting or bypasses platform restrictions.
- AA Browser is GPL-3.0. Any future reuse/adaptation requires license compliance, attribution, marking modified files, and corresponding-source obligations before distribution. Do not copy code until the scope and compliance plan are explicit.
- Therefore no safe, official Android Auto route has yet been identified for exposing a full Browser screen or YouTube video UI through the current media service. Do not promise these capabilities on Android Auto. A compliant audio-only YouTube/media route may be technically different from showing video; investigate only within platform-supported behavior and safe parked-use constraints.
- No source code was changed during this audit. No APK was built. The user-facing head-unit test still shows the app entry but an empty Fermata media root.

## 6C. Fermata Mirror and AUTO flavor audit (2026-11-17)

- Confirmed the AUTO flavor build workflow runs `fermata:packageAutoReleaseUniversalApk`, so it targets the `autoRelease` variant, not the mobile flavor.
- Confirmed `fermata/src/auto/AndroidManifest.xml` declares:
  - `CarService` with CATEGORY_PROJECTION and CATEGORY_PROJECTION_OEM.
  - `MirrorService` with CATEGORY_PROJECTION and CATEGORY_PROJECTION_OEM, label `mirror_service_name`.
  - `MirrorServiceFS` as an AndroidX CarAppService with NAVIGATION category.
  - `ProjectionService` foreground service for MediaProjection.
  - `ProjectionActivity`, `LauncherActivity`, accessibility dispatcher, and Xposed dispatcher.
- Confirmed implementation classes `MirrorActivity`, `MirrorDisplay`, `MirrorService`, and `ProjectionService` are present in `fermata/src/auto`. Thus the source repository contains Fermata Mirror's Android Auto-specific implementation.
- The auto manifest requests Android Auto surface/navigation permissions, media projection foreground service, overlay, accessibility/Xposed-related services and other permissions. Mirror uses MediaProjection and its own CarActivity/Surface integration; it is not the same as the YouTube/Browser web addons.
- Build workflow sets `APP_NAME=AUTO•TQSM`, suffix `.urgent24jam`, and runs the AUTO release universal APK task for arm64 and arm32. Therefore source intent is to build the auto flavor.
- Important diagnostic gap: source presence and Gradle task selection do not prove that the installed APK's merged manifest contains all expected service declarations, that Google Play Services/Android Auto recognizes the projection services, or that the correct launcher entry is selected. Need inspect APK merged manifest / package metadata and compare the installed AUTO•TQSM APK to official Fermata Auto APK. Do not assume missing Mirror classes or change source manifests without evidence.
- Current head-unit photo shows the Fermata Media Service browser root (Folders/Favorites/Playlists, no items). This is distinct from launching the projection-based Mirror service. Need identify whether user opened media app entry or attempted Mirror projection service entry.
- No source code changed and no APK built during this audit. Keep video/mirroring use parked and do not provide instructions to bypass Android Auto driving-safety restrictions.

## 6D. APK artifact and CI metadata audit (2026-11-17)

- Reconfirmed branch HEAD: `d96090f31e7adee7572ff02c31473f93f44d5bb9`; parent `928ca3ac7df99d1b974eaf1371546f946abb1c4b`. This HEAD is documentation-only and is not the commit used by the latest successful APK build.
- Retrieved the arm64 artifact from workflow run `44040206462` (artifact ID `14309775099`; ZIP SHA-256 `155fb5e8f4d94989de34e8f46306e1eb1c1cf8ad8ccdd71a18e6fcaf23e42693`). Artifact is still retained by GitHub. The arm32 artifact is also retained (ID `14308529477`).
- CI job log confirms the APK package metadata: package `me.aap.fermata.auto.urgent24jam`, versionCode `268`, versionName `2.0.2-2b0de5f`, minSdk `28`, targetSdk `36`, compileSdk `36`. It also logs car-app permissions including `androidx.car.app.ACCESS_SURFACE` and `androidx.car.app.NAVIGATION_TEMPLATES`.
- The available CI verification log does not print the merged APK component declarations (services, activities, intent filters, labels). Therefore it does not establish whether `CarService`, `MirrorService`, `MirrorServiceFS`, and the media service are all present in the built APK with the expected declarations.
- The artifact ZIP was retrieved for direct inspection, but the current analysis runtime could not open the downloaded file. Treat merged-manifest inspection as still outstanding; do not infer it from source manifests or the partial CI metadata output.
- No source code was changed and no new APK was built during this audit. Latest successful APK remains from commit `2b0de5f4931579c7f3864f147769b5c85292c0fb`, not current HEAD.

## 6E. Source-path audit: Android Auto media root, add-on registration, Settings (2026-11-17)

- `settings.gradle` includes only dynamic feature module `:web` (not every directory under modules). `:web` registers exactly `YoutubeAddon` and `WebBrowserAddon` in `modules/web/build.gradle`.
- `AddonManager` loads configured add-ons from `BuildConfig.ADDONS` only when their enabled preference is true; the dynamic feature is installed on demand through `DynamicModuleInstaller`.
- `YoutubeAddon` extends `WebBrowserAddon`, and `WebBrowserAddon` is not a `MediaLibAddon`. Neither is registered as a `MediaLibAddon` in the inspected source. `DefaultMediaLib.getChildren(root)` adds only add-ons returned by `AddonManager.getAddons(MediaLibAddon.class)` after last-played, Folders, Favorites and Playlists. This source-level path explains why the Android Auto media browse root can show only those standard containers and no YouTube/Browser entry. This is not evidence of an Android 16 compatibility defect.
- Settings path: `SettingsFragment.addAddons()` iterates `BuildConfig.ADDONS`, creates an `AddonPrefsBuilder`, and adds the add-on enabled toggle. It calls `contributeSettings()` only if the add-on instance is already loaded. This is a source-path observation, not a confirmed explanation for the user's Settings crash; no crash stack trace/log has been captured.
- YouTube playback path: `YoutubeMediaEngine.start()` calls `YoutubeWebView.play()`, which executes JavaScript to call the current HTML video element's `play()`. `YoutubeMediaEngine.playing(url)` updates the media engine callback when a video-playing event arrives. The inspected `YoutubeWebClient` only routes non-YouTube URLs to the Browser fragment. These snippets do not establish why the user's tap added an item to a playlist; no click-event stack trace or runtime log is available.
- No code changes, manifest changes, or new APK build were made in this source audit. Next investigation should inspect the actual APK merged manifest and the UI click/listener path associated with the reported playlist action before proposing a fix.

## 6F. Playback click-path audit (2026-11-17)

- `MediaLibFragment.ListAdapter.onClick()` handles a clicked `MediaItemView`. If the item is a `PlayableItem`, it dispatches to the local `onClick(PlayableItem)`; that method calls `MainActivity.getBody().playItem(i)` unless the same item is already current.
- `MediaSessionCallback.playItem(i, pos)` prepares the item and then `playPreparedItem()` chooses an engine via `EngineManager.createEngine()`, calls `eng.prepare(i)`, and updates the media session queue when the item's parent differs from the previous item's parent. This is the generic media-list click-to-play path.
- `YoutubeFragment` registers a media-service broadcast listener and responds to playable changes, but the inspected fragment source does not show a click handler that would directly explain the user's report that tapping a YouTube result adds it to a playlist.
- Therefore the generic media playback path is present, but there is not enough source evidence to attribute the user's YouTube playlist behavior to it. Need identify the exact UI surface/result type and inspect its click handler; runtime logs would be required to establish the actual event path.
- Safety boundary: do not add video/browser UI or bypass Android Auto restrictions. Android Auto work should remain within supported audio/media browsing behavior; video/browser/projection functionality must not be enabled for use while driving.
- No source code changed and no APK built in this audit.

## 6G. Bluetooth auto-start versus YouTube auto-play audit (2026-11-17)

- `fermata/src/auto/AndroidManifest.xml` declares a `MediaServiceStarter` receiver for `android.bluetooth.device.action.ACL_CONNECTED`.
- `MediaServiceStarter.onReceive()` responds to Bluetooth ACL connection by calling `FermataMediaServiceConnection.connect(null)`; on success it logs that the media service started. It does not issue a Play command or select a YouTube item.
- `FermataMediaServiceConnection.connect()` binds/starts the media service with `ACTION_MEDIA_SERVICE`; the inspected method does not initiate playback.
- `CarService.onDestroy()` pauses active playback and disconnects its service connection. This is lifecycle handling, not an auto-play trigger.
- `YoutubeMediaEngine.playing(url)` handles an already-started YouTube playback event and updates engine/session state; it is not itself the Bluetooth auto-start trigger.
- Conclusion: existing code already has Bluetooth-triggered media-service startup, but source inspection does not show Bluetooth-triggered YouTube auto-play. This distinction is important: starting the service is not the same as starting a selected YouTube audio item.
- Proposed implementation direction (not yet coded): first verify that a valid YouTube audio item/source can be restored and started through the supported media session path after a car connection; gate it behind an explicit user preference and avoid automatic video playback. Bluetooth ACL can include unrelated devices, so do not assume every Bluetooth connection means the car/Android Auto is connected. Prefer a confirmed car/media connection signal where available, with safe fallback and user control.
- No source code changed and no APK built in this audit.

## 6H. User clarification: actual Fermata usage and AUTO•TQSM priority (2026-11-17)

User clarified their real-world Fermata usage:
- They use **Fermata Auto** and **Fermata Mirror** only.
- They hide the **Fermata Media** entry from the Android Auto launcher and do not use it as a user-facing feature.
- AUTO•TQSM's primary target is the **YouTube functionality from Fermata Auto**.
- User would like Fermata Mirror integrated into the same app as Fermata Auto if feasible, but Mirror is optional/lower priority and not a blocker for the YouTube-first app.
- Do not make Fermata Media a visible/user-facing destination or treat it as the user's primary workflow. Distinguish its hidden/internal media service implementation from a launcher entry: an internal service may still support Android media-session or Android Auto integration even when the user hides/does not open its launcher entry.
- This clarification supersedes the earlier strict exclusion of Mirror: Mirror is now an optional integration candidate, subject to feasibility and separate safety/compatibility assessment. First deliver the YouTube-first Fermata Auto experience.
- No source code changed and no APK built as part of recording this clarification.

## 6I. User's three fixed project goals and Auto-vs-Media manifest audit (2026-11-17)

User reaffirmed the three fixed goals:
1. Target Android versions 10–17.
2. RM0.00 user cost.
3. Function on Android Auto head units/players.

Compatibility across Android 10–17 and USB/wireless head-unit connections is a target only; current minSdk 28 proves Android 9+ is permitted by manifest metadata, not that Android 10–17 have all been tested or that all head units work.

Further source audit findings:
- `fermata/src/auto/AndroidManifest.xml` declares `CarService` with `CATEGORY_PROJECTION` and `CATEGORY_PROJECTION_OEM`; this service label is `@string/app_name`.
- `fermata/src/main/res/values/strings.xml` defines the release app name as `AUTO•TQSM` and the media-service label as `AUTO•TQSM Media Service`.
- `fermata/src/main/AndroidManifest.xml` declares `FermataMediaService` separately as an Android `MediaBrowserService`; this explains why a distinct Media Service label exists, but does not by itself explain why the projection `CarService` entry is absent from the user's Android Auto list.
- `fermata/src/main/res/xml/automotive_app_desc.xml` contains only `<uses name="media" />`. This declares media capability; it does not declare a projection service. The projection services are separately declared in the auto flavor manifest.
- The same auto manifest declares `MirrorService` with projection categories and `MirrorServiceFS` as a navigation-category `CarAppService`.
- The auto manifest also declares `<uses-feature android:name="android.hardware.type.automotive" android:required="true" />`. This is a potential compatibility/visibility concern for a phone-based Android Auto app because it marks automotive hardware as required. It is not yet proven to be the cause of the missing `CarService` entry; compare with a known-working official Fermata Auto merged manifest before changing it.
- Workflow explicitly builds `autoRelease` via `fermata:packageAutoReleaseUniversalApk`; package suffix `.urgent24jam` and app name `AUTO•TQSM` are set. This confirms intended flavor selection, not the actual merged manifest contents.
- Strong next diagnostic: inspect the merged manifest of the exact installed AUTO•TQSM APK and compare service declarations, feature flags, labels and package metadata against the user's known-working Fermata Auto APK. The previously downloaded CI artifact could not be opened by the current runtime; do not claim this comparison has been completed.
- No source code was changed and no APK was built during this audit. The likely issue is narrowed to Android Auto discovery/registration of the projection service versus the separately discoverable media service; exact cause remains unconfirmed.

## 6J. Android Auto projection validation audit (2026-11-17)

- Re-fetched the current branch source and confirmed `fermata/src/main/res/xml/automotive_app_desc.xml` contains only `<uses name="media" />`. The auto flavor manifest separately declares `CarService` and `MirrorService` with `CATEGORY_PROJECTION` and `CATEGORY_PROJECTION_OEM`; declaration of those services alone does not guarantee Android Auto will expose them.
- Inspected upstream Fermata issue #846 (opened 2026-10-15, Android Auto 17.7 analysis). Its author reports that Android Auto validates app components by capability type: the media browser is checked as MEDIA, while `CarService` and `MirrorService` are checked as PROJECTION. The report says the "Unknown sources" toggle does not admit PROJECTION apps, even though it can admit MEDIA apps. It also describes an installer/Play-validation gate for sideloaded projection apps and provides a matching log pattern: `CAR.VALIDATOR` / `Package DENIED; failed all other checks`. Source: https://github.com/AndreyPavlenko/Fermata/issues/846
- This is a strong explanation for the user's symptom (Media Service visible, projection entries absent), but it is not yet proven on the user's exact OPPO A3 5G / Android Auto version because no `CAR.VALIDATOR` log or AA version has been captured.
- There are potentially two separate projection checks to resolve before expecting `CarService`/`MirrorService` to appear: (1) the app descriptor in this branch currently declares MEDIA only, not PROJECTION; (2) Android Auto 17.7's reported installer/Play validation may reject sideloaded PROJECTION components even when the descriptor and services are correct. Do not assume fixing only one will make projection appear.
- The `android.hardware.type.automotive required=true` declaration is not identified as the cause by the issue #846 validation analysis. Do not change it as a speculative fix. It is still worth checking against the intended phone/AAOS distribution model separately.
- The same upstream issue says a full Browser/video UI is not an Android Auto media-browser feature. Keep Android Auto interaction within supported audio/media surfaces; do not bypass projection validation, installer checks, or driving-safety restrictions.
- Engineering direction: keep the media service as an internal Android Auto-compatible path, but do not make its empty browse root the user's intended YouTube UI. Investigate a compliant audio-only YouTube `MediaLibAddon`/browse implementation and verify that the descriptor declares only capabilities the app actually implements. Treat Mirror as optional and subject to the same projection validation gate.
- No code or manifest was changed and no APK was built in this audit. No installer workaround is approved or required by this project; preserve the user's no-Shizuku/no-Developer-Options requirement and RM0 target.


## 6K. YouTube media-library integration feasibility audit (2026-11-17)

- Re-inspected the exact current branch implementation. `YoutubeAddon` extends `WebBrowserAddon` and implements only `PreferenceStore.Listener`; it does not implement `MediaLibAddon`. `WebBrowserAddon` is a phone-side fragment addon, not an Android Auto media-library provider.
- `DefaultMediaLib.getChildren(Root)` adds the last-played item, Folders, Favorites, Playlists, and then only addon roots implementing `MediaLibAddon`. The `MediaLibAddon` contract requires `isSupportedItem`, `getRootItem`, and `getItem`.
- A working upstream example exists in the optional TV module: `TvAddon implements MediaLibAddon`, and `TvRootItem` extends `ItemContainer`. This proves the extension pattern is available in the codebase, but does not make it appropriate to adapt TV/video content for Android Auto.
- The current YouTube engine is explicitly video-oriented: `YoutubeMediaEngine.Current` is created from the active YouTube page URL; its base `YoutubeItem.isVideo()` returns true; `YoutubeMediaEngine.start()` calls `YoutubeWebView.play()`, which controls the page's HTML video element. `YoutubeFragment` also restores/reopens the last video when the car activity is created, with autoplay disabled on fresh car launch. These source facts show the current implementation is not an audio-only media source.
- Android for Cars official documentation describes the media browser hierarchy as browsable/playable audio content controlled through the media session. Android Auto's media app interface is not a general-purpose video/browser surface. Sources: https://developer.android.com/training/cars/media and https://developer.android.com/training/cars/media/create-media-browser/content-hierarchy
- Decision: do not expose the current `YoutubeItem` as a playable Android Auto media-library item, because it is marked as video and starts the page video element. Do not try to hide the video surface while retaining a video playback path for use while driving.
- A compliant next design would require a genuinely audio-only source and media item implementation, separate from the existing YouTube WebView video engine, with supported media-session behavior and no video autoplay. The current source audit does not establish that such an audio-only YouTube stream path exists, nor that it can be implemented within RM0 and platform terms. Do not claim it is ready.
- Keep the phone-side YouTube WebView as a distinct feature; treat any browser/video projection or mirroring as parked-only and subject to platform/device support. Mirror remains optional and is not a substitute for a compliant media browsing implementation.
- No source code was changed and no APK was built during this audit. This is an architecture/feasibility finding, not a runtime test.


## 7. Work remaining (priority order)

1. Preserve this master file and update it after every material change.
2. Re-audit the Settings crash path after the base addon registry change; obtain logs if the cause remains ambiguous.
3. Trace the YouTube click/selection/playback path and fix only after identifying the actual cause.
4. Complete the AA Browser source audit: manifest, activities, services, launch intents, permissions, Android version checks, licensing and attribution. Integrate only the minimum compatible code that is needed and permitted.
5. Verify Android Auto media/browser visibility and launch behavior against official platform requirements. Do not promise unsupported video/browser projection behavior.
6. Build and verify APKs only after targeted fixes are ready.
7. Test install and app behavior on phone, then test on the actual Android Auto head unit. Keep CI build success separate from device-test success.
8. Record test date, phone OS, Android Auto version, head-unit model, exact steps, result and evidence.
9. Only after stable real-device testing, consider future upgrades. Keep MATAMIERA as a separate project with its own master document.

## 8. Change-control rules for any future assistant/developer

- Read this file before editing code.
- Preserve the user's exact scope and wording: one YouTube, one Browser, one APK.
- Do not change logos, shapes, colors, branding, or UI artwork unless explicitly asked.
- Do not mix MATAMIERA or other projects into this repository.
- Do not remove a Gradle property or registry unless all consumers are checked first.
- Before every edit: inspect the exact current branch/file and confirm the expected old content. Make a narrow change.
- After every edit: report the file, reason, commit SHA, and CI result.
- Never claim an issue is fixed based only on source edits or a successful CI build.
- If uncertain, stop and ask one specific question rather than guessing.
- Keep a short dated change log below.

## 9. Change log

- 2026-11-17: Current branch source confirmed to select only `:web` and register exactly one YouTube addon plus one Web Browser addon.
- 2026-11-17: Base addon registry corrected to `addons = []` in commit `2b0de5f4931579c7f3864f147769b5c85292c0fb`.
- 2026-11-17: GitHub Actions run `44040206462` completed successfully and uploaded arm64 and arm32 APK artifacts. No head-unit runtime test is recorded.


## 6L. Clarification: app identity and integrated components (2026-11-18)

- User explicitly clarified that **AUTO•TQSM is the user's own app**. Fermata is a source of reusable components/framework code integrated into AUTO•TQSM; the user is not merely renaming the separate Fermata Auto app, nor launching a separate Fermata app to obtain YouTube.
- The phone-side YouTube functionality being discussed is the YouTube feature inside the AUTO•TQSM app/package. In source terms, it is implemented using integrated Fermata-derived components; that implementation origin must not be confused with a separate user-facing Fermata app.
- The user's head-unit photos show the AUTO•TQSM Media Service entry enabled in Android Auto's Customize Launcher and opening a media browser surface with Folders/Favorites/Playlists but no items. This is distinct from the phone-side AUTO•TQSM YouTube feature.
- Future analysis and reports must use these terms precisely: AUTO•TQSM = the app; Fermata = integrated component/framework source; YouTube = AUTO•TQSM's phone-side feature; Media Service = the separate Android media-browser integration surface exposed by the same app package.
- No source code, manifest, logo, or branch configuration was changed for this clarification. No APK was built or runtime-tested as part of this documentation update.


## Fermata Auto architecture study — source findings (2026-11-18)

Scope: read-only study of the Fermata-derived code currently present on branch `urgent24jam`. These are source facts, not a claim that the user's separate phone/player runtime was tested.

### A. Android Auto app/service entry points
- `fermata/src/auto/AndroidManifest.xml` declares the Auto-specific projection and mirror services, LauncherActivity, projection service, accessibility/Xposed dispatch services, and Bluetooth ACL-connected receiver `MediaServiceStarter`.
- `FermataMediaService` is implemented as an AndroidX `MediaBrowserServiceCompat`. In `onCreate()` it creates `DefaultMediaLib`, a `MediaSessionCompat`, `MediaSessionCallback`, calls `setSessionToken()`, and installs the session callback.
- `onGetRoot()` returns the library root ID and advertises media search and list-style browsable/playable content hints. The service's browse data therefore comes from `DefaultMediaLib`, not from the phone launcher registration alone.

### B. Media browse tree
- `DefaultMediaLib.getChildren(root)` builds root children from last-played item (if any), Folders, Favorites, Playlists, and then roots supplied by loaded add-ons implementing `MediaLibAddon`.
- Child browse requests resolve a media ID to a `BrowsableItem` and convert its children to MediaBrowser items. Non-browsable IDs return an empty list.
- `MediaLibAddon` defines three key integration methods: `isSupportedItem(Item)`, `getRootItem(DefaultMediaLib)`, and `getItem(DefaultMediaLib, scheme, id)`. This is the extension contract for adding a navigable item tree to the Media Service.
- `AddonManager.getAddons(MediaLibAddon.class)` filters only the loaded add-ons that implement that interface. It loads configured add-ons from `BuildConfig.ADDONS`, with Auto-specific filtering controlled by `BuildConfig.AUTO`.

### C. YouTube phone feature vs media-session playback
- `YoutubeFragment` is the phone-side YouTube page: it creates and initializes `YoutubeWebView`, `YoutubeWebClient`, and `YoutubeChromeClient`, loads the mobile YouTube URL, restores saved state, and registers a listener with the media-service UI binder.
- `YoutubeMediaEngine` connects the page's video lifecycle to the shared `MediaSessionCallback`: video-playing events set the engine/source, engine start calls `web.play()`, pause calls `web.pause()`, and current metadata/duration/position are read from the WebView.
- The engine's `YoutubeItem` is explicitly `isVideo() == true`. The engine uses an internal `ExtRoot("youtube", lib)` and temporary current/next/previous/end items for playback/session control.
- `YoutubeAddon` extends `WebBrowserAddon`; its source declaration does not implement `MediaLibAddon`. `WebBrowserAddon` is a fragment/settings addon, not a media-library browse provider.
- Consequence: the existing YouTube page can participate in the shared playback session when a video is playing, but the existing YouTube addon does not contribute a YouTube browse root to `DefaultMediaLib`. This distinction explains why a phone-side YouTube feature and an enabled Media Service launcher entry can coexist with an empty browse tree. It is a strong code-level explanation consistent with the supplied photos, not proof of all possible runtime causes.

### D. Important boundaries
- Media-session playback control/metadata and MediaBrowser browse-tree population are separate pathways. A current session item does not automatically create a browsable YouTube catalogue.
- Any future integration must be designed around permitted Android Auto media content and driving-safety requirements. The existing YouTube items are video items; do not misclassify them as audio or expose video playback while driving as a workaround.
- This study made no source, manifest, logo, or branch changes. It did not build a new APK or perform device runtime testing.

### E. Technical follow-up points for implementation review
1. Trace how the app-specific AUTO•TQSM module/package wires the integrated YouTube page to its media-session binder in the actual build flavor.
2. Decide whether AUTO•TQSM needs a legitimate browsable audio/media source, rather than assuming its existing video WebView can be listed as a standard Android Auto media catalogue.
3. If a compliant browsable provider is implemented, define stable media IDs, root/child containers, item metadata, supported playback actions, and lifecycle/refresh notifications; test on phone and head unit separately.


## 6M. Root dispatcher call-chain recheck (2026-11-18)

- Re-fetched the exact active and preserved branch versions of `EventDispatcher.java`, `Su.java`, `AccessibilityEventDispatcherService.java`, `ProjectionActivity.java`, `XposedEventDispatcherService.java`, and `MirrorDisplay.java`.
- Active and backup branch blob SHAs for `EventDispatcher.java` are identical: `bbd7567e216cb0512a66a93973cd6b5208d6ec77`. Active and backup `Su.java` are also identical: `4c3d0993fe0443017441e21bc6dfc898611314c1`. These two files have not diverged between the compared branches.
- Confirmed in `EventDispatcher.get()`: multi-window dispatch selects Accessibility; otherwise an active Fermata Activity uses the in-app Activity dispatcher; if no active Fermata Activity is available, the Xposed dispatcher is selected. Xposed dispatch falls back to Accessibility for touch/motion events when Xposed cannot dispatch.
- Confirmed in `AccessibilityDispatcher`: each Back/Home/Tap/Motion path first tries the Accessibility service; only if that path returns false does it ask `SuDispatcher.get()` for a Root fallback.
- Confirmed `SuDispatcher.instance` is initialized as `completedNull()`. It becomes non-null only when `EventDispatcher.useSu(Su)` is called. The method `useSu(Su)` remains a definition in `EventDispatcher.java`; no invocation has been identified in the AUTO module files reviewed so far.
- Confirmed `Su.get()` starts an Android `su` process and creates a persistent command writer only when its asynchronous authorization/probe succeeds. This is a rooted-device-specific capability, not a general Android permission or non-root dispatch mechanism.
- The recursive Git tree for `urgent24jam` was retrieved successfully: tree SHA `c65f8480ad6031402d23d2dc980b9bab4b8b1bdf`, `truncated=false`, 799 tracked blobs and 453 Java files. This is a complete path inventory, but it does not include source contents. GitHub code search still searches the default branch, not this active branch.
- Therefore, the complete repository-wide source call-site scan is STILL NOT DONE. Do not state that `useSu()` or `Su.get()` has no caller anywhere in the repository. The current confirmed result is limited to the AUTO module files and the explicitly re-fetched Mirror/dispatcher files.
- No application source, manifest, settings, YouTube/Browser code, or logo assets were changed. No build or device test was run. No APK is verified by this recheck.
- Next: obtain a complete branch checkout or a branch-aware source-content search to scan all 453 Java files (and any Kotlin/JavaScript callers if relevant). Until then, preserve the current dispatcher and Accessibility behavior; do not add or remove Root initialization or alter Xposed fallback.


## 6N. AUTO source-scope Root call-site scan (2026-11-18)

- Retrieved the recursive Git tree for `urgent24jam` and fetched every Java file under `fermata/src/auto/java/` by its exact blob SHA.
- All 16 AUTO Java files were fetched successfully; no fetch errors.
- Exact-token scan across those 16 files found Root-related references in only two files: `EventDispatcher.java` and `Su.java`.
- In `EventDispatcher.java`, `useSu(Su)` appears as a method definition (lines 29-30); the five `SuDispatcher.get()` calls are fallback reads in Accessibility dispatch methods (lines 194, 201, 208, 220, 228). `SuDispatcher.instance` is initialized with `completedNull()` (line 234). No invocation of `useSu()` was found in any of the 16 AUTO Java files.
- In `Su.java`, `Su.get()` is defined (line 28) and internally constructs `new Su(...) ` after its `su -c ls` probe succeeds (line 35). No call to `Su.get()` was found in the 16 AUTO Java files.
- Therefore, within the complete AUTO Java source directory, Root fallback is present but its activation path is not connected by a discovered caller. This is a scoped result only; it does NOT prove there is no caller in other repository source sets or generated/injected code.
- Dispatcher selection remains: multi-window -> Accessibility; active Fermata Activity -> Activity dispatcher; otherwise -> Xposed dispatcher. Xposed dispatch has Accessibility fallback. Accessibility's Back/Home/Tap/Motion handlers try the Accessibility service first, then consult the Root dispatcher only if that attempt fails.
- No source code, manifest, settings, YouTube/Browser code, or logo assets changed. No build or device test was run; no APK is verified.
- Next: inspect the remaining source sets for references/imports to `EventDispatcher.useSu`, `Su.get()`, or reflection/integration hooks that could activate Root. Keep existing dispatcher behavior unchanged until that cross-source check is complete.
