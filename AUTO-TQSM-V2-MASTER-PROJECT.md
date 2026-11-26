# MASTER PROJECT — •𝙰ᴜᴛᴏ•ᴛϙ𝘴𝙼• (𝘝²)•

**Project status:** New V2 project — foundation recorded  
**Created:** 2026-11-26  
**Repository:** `androidautoplayer-mie/Fermata`  
**Working branch:** `auto-tqsm-v2-clean-start`  
**Base:** repository `master`; sampled build files compared with upstream `AndreyPavlenko/Fermata`

## 1. Project identity

- Official project and application name: **•𝙰ᴜᴛᴏ•ᴛϙ𝘴𝙼• (𝘝²)•**
- The nine visual references supplied by the owner are the project's identity references.
- Preserve the supplied logo artwork, lettering, and color identity. Do not redraw, recolor, or replace without explicit instruction.

## 2. Core concept — owner-confirmed

- The application concept is Fermata Auto.
- The intended product is one integrated phone application/package under the AUTO•TQSM V2 identity.
- Fermata is the upstream application/source baseline, not a second user-facing app that users must open to access AUTO•TQSM functions.
- Do not add unrelated applications, features, or project components without the owner's direction.
- Do not treat old AUTO•TQSM experiments, MATAMIERA, ARAD, or other projects as part of this V2 project.

## 3. Visual identity and display behavior

- The official logo is used in the mobile app and is also the app identity shown in the Android Auto display when connected.
- The front/home display has day and night presentation that follows the vehicle lighting state.
- The supplied app/display identity images define the visual direction for the Android Auto-connected display.
- The supplied nine images remain reference material; implementation details must be checked against the actual assets before use.

## 4. Recipient-specific installation code — owner-confirmed concept

- The same APK may be sent to different recipients by email.
- Each recipient receives an individual installation code.
- A code is assigned to its intended recipient, cannot be used by another recipient, and is redeemable once.
- After successful redemption, the code is marked used and cannot be redeemed again.
- The service must record recipient reference, code record/hash, redemption status/time, and registered device reference.
- Never store raw codes as ordinary database text or expose them in logs.
- A separate Contact box on the player displays the owner's contact information.
- Contact is independent from the installation-code mechanism.

### Legal/security gate

The upstream project is GPL-3.0. Before implementing recipient activation, review whether any activation requirement would impose additional restrictions on recipients' GPL rights. Do not use activation to deny rights granted by the applicable license. The final design must preserve required source-code access, license notices, modification notices, and other GPL obligations. Obtain qualified legal review for the activation/distribution model before public distribution. This is a design gate, not a conclusion that the proposed feature is prohibited.

## 5. Upstream source and licensing

- Upstream repository: https://github.com/AndreyPavlenko/Fermata
- Upstream license file: GPL-3.0.
- Upstream README describes Fermata as a free/open-source media player with Android Auto and Android TV support and gives the Gradle build workflow.
- Preserve upstream copyright and license notices.
- Mark modified files and relevant modification dates.
- Make the corresponding source available in the manner required by GPL-3.0 when distributing covered binaries.
- Maintain a clear inventory of upstream files, modifications, new files, and third-party dependencies/licences.
- This project record is engineering documentation, not legal advice.

## 6. Engineering baseline

- A new isolated working branch `auto-tqsm-v2-clean-start` was created from the personal repository's `master`.
- The following sampled files in personal `master` had identical Git blob SHAs to upstream `AndreyPavlenko/Fermata` at the time of comparison:
  - `fermata/src/main/AndroidManifest.xml`
  - `fermata/build.gradle`
  - `gradle/libs.versions.toml`
  - `settings.gradle`
  - `build.gradle`
- This is a sampled comparison only; it does not prove every file or commit in the branch is identical to upstream.
- No source code, manifest, app resources, or APK has been changed or built for V2 in this initial setup.

## 7. Development sequence

1. Complete a full baseline audit against the official upstream source, including submodules, modules, manifests, build variants, dependencies, and licences.
2. Record the exact upstream commit and a reproducible source snapshot.
3. Map the supplied nine visual references to the app screens/assets they represent.
4. Prepare the app identity plan: application label, package/application ID strategy, launcher identity, and Android Auto display identity.
5. Review day/night behavior and the vehicle-lighting signal available to the app.
6. Design the recipient-code and Contact system only after the GPL compatibility review; keep secrets server-side and use secure one-time redemption.
7. Implement changes in small, traceable commits.
8. Build debug APKs, inspect build output, then test on phone and the actual Android Auto head unit.
9. Release only after functional tests, license/source-distribution checks, and owner acceptance.

## 8. Project governance

- This V2 project is separate from all other projects.
- The owner controls product decisions and feature scope.
- Do not silently change the concept, add features, merge branches, or replace assets.
- Distinguish confirmed facts, proposals, assumptions, and unverified results in every report.
- Every code change must report the files changed, reason, commit, build/CI result, and test status.
- A successful build is not proof of successful runtime behavior.
- Never claim an APK or feature is ready until it has actually been built and tested.

## 9. Initial status

**Confirmed:** project name, Fermata Auto concept, identity references, day/night display concept, Android Auto display identity, recipient-bound one-time code concept, and separate Contact box.

**Recorded:** isolated V2 branch and initial master-project record.

**Not yet done:** full upstream audit, code changes, activation service, visual implementation, APK build, device testing, and release.


## 10. Initial upstream source inspection — 2026-11-26

Read-only inspection of the official upstream repository `AndreyPavlenko/Fermata`:

- Upstream README describes one Fermata Media Player application with Android Auto and Android TV support, and documents the Gradle build workflow.
- The upstream repository's `LICENSE` is GNU GPL version 3.
- Current upstream README/source snapshot identifies app version 2.0.2, version code 268, minimum SDK 28, target SDK 36, compile SDK 36.
- The root build configuration already supports application identity overrides through `APP_NAME`, `APP_ID`, and `APP_ID_SFX`. This may allow a controlled rebrand without rewriting the core application.
- The main manifest uses the app label resource and launcher drawable; Android Auto metadata and the media-service small icon are separately declared. Therefore the identity audit must include the phone launcher, Android Auto media-service label/icon, Mirror service labels/icons, and any other user-visible names—not just the app title.
- Auto-specific manifest includes Fermata's projection/car services and related declarations. Preserve these functions unless a specific, tested reason requires a change.
- App strings separately define mobile name, Android Auto name, media-service name, Mirror names, and addon labels. A consistent rebrand requires checking all these entry points.
- Five sampled build/source files in the personal fork's `master` have identical blob SHAs to the corresponding upstream files: main manifest, app build.gradle, version catalog, settings.gradle, and root build.gradle. This confirms those five files match; it is not a full-repository equivalence claim.

### Initial implementation direction (not yet applied)

1. Prefer upstream-supported Gradle identity overrides where sufficient; avoid broad source renaming.
2. Audit all user-visible strings and launcher/service icon references before making a branding patch.
3. Keep the original Fermata package namespace internally unless a package-ID change is deliberately designed and tested. Package/application ID strategy affects upgrades, Android Auto recognition, data migration, and signing.
4. Do not change the existing core media, YouTube, Browser, Mirror, or Android Auto behavior as part of a branding-only change.
5. The supplied AUTO•TQSM logo artwork must be obtained in its original usable image format and mapped to the required Android icon densities/adaptive icon assets without altering its design.
6. Build and install a debug variant first; test mobile launch, Android Auto listing, media service identity, day/night behavior, and upgrade/install behavior before preparing a release build.

### Not yet verified

- Complete source-tree equivalence with upstream.
- Exact commit SHA of the upstream snapshot used for V2.
- Submodule revisions and third-party licence inventory.
- Full list of user-visible strings/icons and any hard-coded Fermata branding.
- Actual day/night response to vehicle lighting on the target head unit.
- Recipient activation architecture and its GPL-3.0 compatibility.
- Build success, APK output, phone runtime, and Android Auto runtime.

No application source, manifest, resources, or APK were changed during this inspection.


## 8. Additional component research — 2026-11-26

Owner-authorized reference projects:
- AA Browser: https://github.com/kododake/AABrowser
- FermataX Auto: https://github.com/chuoinho/FermataX

### FermataX findings

The inspected FermataX README describes it as a GPL-3.0 fork of Fermata Media Player and a car-oriented media hub. Its documented areas include:
- Dashboard-first Android Auto interface and customizable navigation rail.
- Adaptive Now Playing / Continue / Recent presentation.
- Local media, folders, playlists, favorites and playback progress.
- YouTube and Web media access integrated with the media hub.
- MediaSession, player controls and voice navigation.
- A CI pipeline with mobile/auto tests, UI/architecture guards, lint and debug APK generation.

The README advertises Android 9.0+ in its badge, but also explicitly says compatibility depends on Android version, Android Auto version, device manufacturer and current security policies; its installation path may require KingInstaller and may not work on every device. Therefore the badge alone is not proof that every FermataX feature or Android Auto projection works on Android 9–16. Source-level compatibility and device testing remain necessary.

Potentially useful concepts to inspect in source:
1. Dashboard and navigation-rail architecture.
2. Adaptive Auto layout and width-aware controls.
3. Unified media/session state across phone and Auto.
4. Recent/favorites/playback-progress integration.
5. CI test separation between mobile and Auto.

Do not import all FermataX features merely because they exist. The V2 scope remains the owner's Fermata Auto concept; unrelated IPTV, Stalker, Xtream, Stremio, podcast, audiobook or other service modules are out of scope unless the owner later requests them.

### AA Browser findings

The inspected official kododake/AABrowser README states Android 15 or later as the current requirement. It documents a WebView-based browser experience for Android Auto with:
- Automotive-first controls and layout.
- Tabs and session restoration.
- Bookmarks / quick-link dashboard.
- Desktop/mobile mode and display scaling.
- Fullscreen video / Widevine L3 support.
- Light and AMOLED-dark presentation.

The README explicitly limits use to passengers or when safely parked. These are candidate UX and browser-feature references, not proof that the implementation can be integrated unchanged into the Fermata codebase.

Compatibility evidence:
- Current AA Browser README: Android 15+.
- Its historical v1.3 was reported by its maintainer as Android 10+ at the phone-install level, but the maintainer raised the minimum to Android 15 from v1.5 because of head-unit display failures on older devices.
- An issue requesting Android 12–14 support was closed as not planned.
- Therefore do not promise AA Browser's Android Auto display behavior on Android 10–14 by simply lowering minSdk. A separate compatibility design and real device testing are required.

### V2 Android compatibility target — owner-requested, not yet verified

- Phone installation/runtime target: Android 10 through Android 16.
- Android Auto projection target: phone connected to a compatible Android Auto head unit.
- These are two separate compatibility checks. An APK installing on Android 10 does not prove that its Android Auto entry, service, browser or media UI will appear/work on that OS.
- Build strategy should retain the broadest viable phone minSdk, isolate newer-API features behind version checks, and avoid importing a dependency whose minSdk exceeds Android 10 unless it can be safely isolated or replaced.
- Test matrix must cover Android 10, 11, 12, 13, 14, 15 and 16 for phone installation and core functions; separately record Android Auto app/version, head-unit model, wired/wireless mode, launcher visibility, media browsing, playback and day/night response.
- Any unsupported combination must be recorded honestly rather than described as compatible.

### Initial component selection

Candidate concepts for detailed source audit:
- FermataX: Auto dashboard architecture, media session/state integration, navigation rail, adaptive layouts, CI checks and tests.
- AA Browser: car-screen browser controls, tab/session model, bookmarks/quick links, display scaling, light/dark UI, fullscreen video handling.

No source code has been copied, merged, or modified from either reference project in this research step. Before any code reuse, compare actual source files, dependencies, copyright notices and GPL-3.0 obligations. Prefer selective, attributable implementation over bulk copying.

## 9. Compatibility acceptance criteria

A V2 build is not declared compatible based only on Gradle minSdk or a successful APK build. For each Android version 10–16, verify:
1. APK installs and launches on phone.
2. Core phone screens and settings operate without crash.
3. Android Auto connection is detected and app/service identity appears where supported.
4. Each supported Auto function opens and responds correctly.
5. Media session and playback controls behave correctly.
6. Day/night presentation responds correctly to the supported vehicle/Android Auto signal.
7. Disconnect/reconnect does not leave the app in a broken state.

Record results per OS version and head unit. No compatibility claim until the relevant test passes.


## 11. First implementation change — 2026-11-26

**Commit:** `a89144a5a2b1f4f3f453eae5ffd092b9831b811e`

Changed only release display-name strings in `fermata/src/main/res/values/strings.xml`:
- Mobile application label: `•𝙰ᴜᴛᴏ•ᴛϙ𝘴𝙼• (𝘝²)•`
- Android Auto application label: `•𝙰ᴜᴛᴏ•ᴛϙ𝘴𝙼• (𝘝²)•`
- Mirror service label: `AUTO•TQSM MIRROR`
- Media service label: `AUTO•TQSM MEDIA SERVICE`

Debug labels remain visibly marked as debug builds. No package ID, app icon, Mirror FS/navigation service label, media behavior, browser, YouTube, permissions, or activation code has been changed.

**Build/test status:** not yet built or tested. This is a release-label branding change only; it does not prove the logo assets or runtime Android Auto entries are correct. Next gate: run the existing Gradle build/CI, inspect merged manifests and generated APK labels, then continue with asset mapping and functional source audit.
