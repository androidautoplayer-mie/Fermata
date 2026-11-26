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
