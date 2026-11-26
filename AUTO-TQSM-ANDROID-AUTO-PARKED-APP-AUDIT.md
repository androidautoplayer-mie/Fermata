# AUTO•TQSM — Android Auto parked YouTube/Browser feasibility
Date: 2026-11-26
Repository: androidautoplayer-mie/Fermata
Branch: auto-tqsm-rebuild-from-build95
Base: Build #95 commit 848fde714b88fd39eb2f76ee956b303899c16633

## User requirement
- Keep one AUTO•TQSM phone APK.
- YouTube and Browser must appear on the Android Auto head-unit display.
- Both must be usable only while the vehicle is parked.
- The user is not asking to use either app while driving.
- Do not add unrelated features or change the approved branding.

## Source audit on this branch
- `fermata/src/main/AndroidManifest.xml` declares the existing Fermata host and Android Auto media application metadata. The main activity has LAUNCHER and LEANBACK_LAUNCHER filters, but no CAR_LAUNCHER category or APP_BROWSER category.
- `fermata/src/auto/AndroidManifest.xml` contains the existing Fermata projection services and automotive feature declaration. These declarations do not, by themselves, make YouTube or Browser supported parked apps.
- `modules/web/src/main/AndroidManifest.xml` declares the web dynamic feature.
- The `:web` feature registers `YoutubeAddon` and `WebBrowserAddon`. The YouTube addon is a phone-side web/video addon; the Browser addon is a phone-side WebView addon. Neither is a general Android Auto parked-app implementation.
- Existing media browsing services are for the Android Auto media/audio category, not arbitrary browser or video activities.

## Current official Android platform support (checked 2026-11-26)
- Android Auto supports parked app activities on Android 15+ only for the currently supported parked-app categories. Current official documentation identifies Games as the only parked category supported on Android Auto.
- Video parked apps are supported on Android Automotive OS, not Android Auto.
- Browser parked apps are supported on Android Automotive OS (currently beta), not Android Auto.
- Android Auto media apps are a separate audio category; a MediaBrowser/MediaSession entry cannot legitimately turn a video player or browser UI into an Android Auto parked app.
- Official references:
  - https://developer.android.com/agents/skills/cars/android-for-cars-parked-apps/skill
  - https://developer.android.com/training/cars/parked/auto
  - https://developer.android.com/training/cars/platforms/android-auto
  - https://developer.android.com/training/cars

## Engineering decision
Do NOT add a fake `CAR_LAUNCHER` declaration, empty media-library entries, or manifest labels that imply YouTube/Browser parked support on Android Auto. Those changes would not implement the requested behavior.
Do NOT repurpose the existing video engine as an audio media source.
Do NOT use projection/mirroring or other workarounds to evade platform parked-app category restrictions.

## Status
- Build #95 baseline preserved.
- This branch is isolated from `urgent24jam` and other projects.
- This commit records the verified platform blocker and source audit only.
- No APK was built by this change.
- No YouTube/Browser Android Auto parked-app functionality is claimed as implemented.
- Revisit implementation only if Android Auto officially adds video and browser parked-app categories, or if the target head unit is confirmed to run Android Automotive OS rather than Android Auto projection.
