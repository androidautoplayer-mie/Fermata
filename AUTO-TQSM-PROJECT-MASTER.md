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
- Current HEAD: `2b0de5f4931579c7f3864f147769b5c85292c0fb`
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
