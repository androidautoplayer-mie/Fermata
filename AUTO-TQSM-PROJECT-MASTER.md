# AUTO•TQSM — PROJECT MASTER / HANDOVER

Last updated: 2026-11-17 (UTC)
Repository: https://github.com/androidautoplayer-mie/Fermata
Working branch: `urgent24jam`
Project owner: user
Purpose: a single Android Auto-oriented APK with exactly one YouTube component and one Browser component.

## 1. Non-negotiable scope

- App name shown by the build workflow: `AUTO•TQSM`.
- Deliver one APK/app package, not a collection of separate user-facing apps.
- Include exactly one YouTube addon and one Web Browser addon.
- Keep the existing Fermata host/framework where needed to host the addons; do not rebuild the whole application from scratch without a technical reason.
- Keep the YouTube "Try to Skip Ad" functionality already present in the YouTube addon, subject to its actual implementation and applicable platform rules.
- No Shizuku requirement.
- Do not require the user to enable Android Developer Options as a prerequisite.
- Budget target: RM0.
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
- Current HEAD: `d96090f31e7adee7572ff02c31473f93f44d5bb9` (documentation-only audit commit; no APK built from this HEAD).
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
