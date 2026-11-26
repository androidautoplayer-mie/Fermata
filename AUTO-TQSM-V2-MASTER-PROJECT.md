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
