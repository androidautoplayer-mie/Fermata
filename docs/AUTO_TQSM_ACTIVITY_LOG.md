# AUTO TQSM — Project Activity Log

Project branch: `urgent24jam`  
Repository: `androidautoplayer-mie/Fermata`  
Log opened: 2026-11-18

## Standing rules

- Preserve existing application functionality. Do not remove or redesign existing YouTube, Browser, or unrelated app features.
- Keep improvements isolated to TQSM Mirror unless a wider change is explicitly required and explained first.
- Zero-cost requirement: no paid services, subscriptions, or new user costs.
- Do not bypass Android security controls; use official Android permission flows.
- Do not mark a feature complete until supported by build evidence and, where applicable, real-device testing.
- Record meaningful work here: date, scope, files, commit IDs, CI run links/results, device tests, and known limitations.

## Activity

### 2026-11-18 — Mirror permission-flow review

- Reviewed the AUTO TQSM Mirror permission flow.
- Found a mandatory Root setup step (`requestRootPermission()`) in the projection startup chain. This conflicted with the goal of not requiring Root or Xposed for ordinary users.
- Confirmed that Android MediaProjection consent must remain an official Android user-approved flow.
- Accessibility remains relevant to the existing touch-control path. Removing a Root requirement does not itself provide an alternative touch-control route.

### 2026-11-18 — First isolated code change

- Commit: `e5bdc63974924008ef621037416e02529bf0713b`
- File: `fermata/src/auto/java/me/aap/fermata/auto/ProjectionActivity.java`
- Change: removed the mandatory Root setup step from the projection startup permission chain. Overlay/write-settings checks and the official MediaProjection consent request remain.
- Existing YouTube, Browser, and unrelated app modules were not intentionally changed by this commit.
- CI Build #49: [run 44225850452](https://github.com/androidautoplayer-mie/Fermata/actions/runs/44225850452) — cancelled.
- CI Build #50: [run 44226090742](https://github.com/androidautoplayer-mie/Fermata/actions/runs/44226090742) — cancelled during the `Build APKs` step. SDK/Gradle setup steps completed, but APK build did not complete. This is not a successful build.
- Not complete: compatibility diagnostics and a supported alternative touch-control route for devices where Accessibility cannot be enabled.
- Not verified yet: runtime behavior on physical phones and Android Auto head units.

### 2026-11-18 — Mirror touch-dispatch audit (read-only)

- Reviewed `ProjectionActivity.java`, `ProjectionService.java`, `MirrorService.java`, `MirrorActivity.java`, `MirrorDisplay.java`, `EventDispatcher.java`, and `AccessibilityEventDispatcherService.java` on branch `urgent24jam`.
- Confirmed the projection startup chain now requests overlay permission, write-settings permission, and then the official MediaProjection consent. It no longer calls the Root setup step.
- Confirmed head-unit touch events enter through `MirrorDisplay.motionEvent()`, are translated to phone display coordinates, and are passed to `EventDispatcher`.
- Confirmed an in-app `ActivityDispatcher` path exists when a supported Fermata activity is active. If that route is unavailable, the current dispatcher can fall back to Xposed and then Accessibility; Accessibility also handles multi-window and gesture cases. Root dispatch remains present in existing source.
- Important limitation: removing the mandatory Root step does not create a new non-Accessibility touch route for all devices. The current code does not yet expose a reliable user-facing capability check for display-only versus touch-control operation.
- No code was changed during this audit. No YouTube, Browser, or unrelated module was edited.
- Next engineering task: design a non-invasive capability/status check around the existing dispatcher selection, without changing dispatch behavior until each branch is understood. Investigate alternative input routes separately; do not assume Bluetooth HID works without device/player testing.
- Physical-device testing remains outstanding.

## Next steps

1. Obtain an uninterrupted CI run for the current code commit and record the actual result.
2. Inspect the exact dispatcher selection conditions and activity/window lifecycle to define accurate capability states.
3. Design a compatibility check that distinguishes screen display from touch-control capability without changing existing dispatch behavior.
4. Investigate legitimate supported alternative input routes; do not assume Bluetooth HID works without evidence.
5. Add clear in-app status and guidance for unsupported control modes, isolated to Mirror.
6. Test on physical devices and record phone model, Android version, USB/wireless connection, display result, and touch result.

## Ongoing record

Append each meaningful AUTO TQSM project activity below. Keep commit hashes and CI results exact; distinguish code changes, successful builds, and real-device verification.


### 2026-11-18 — Retry cancelled CI job

- Build #51 / run [44226613562](https://github.com/androidautoplayer-mie/Fermata/actions/runs/44226613562) was cancelled during the `Build APKs` step; setup steps succeeded, but APK build did not finish.
- Retried the cancelled `Build Fermata Auto APKs` job using GitHub Actions job retry. GitHub accepted the retry request.
- Retry outcome is pending; do not treat it as a successful build until the new attempt completes and APK verification/upload steps succeed.


### 2026-11-18 — Preserve pre-change Mirror state

- User raised a valid concern that experimental work should be set aside rather than discarded.
- Preserved the exact pre-rootless-change repository state in branch `tqsm-mirror-before-rootless`, created from parent commit `51a3101a8c8329e5ec9761a9718bb16bf7687beb`.
- The experimental change remains in `urgent24jam` at commit `e5bdc63974924008ef621037416e02529bf0713b`; it removed the mandatory Root permission step from projection startup and removed its helper method, but did not remove the existing dispatcher classes or Accessibility service paths.
- No YouTube or Browser code was changed by this experiment.
- Going forward, keep experimental changes isolated and retain a known-good fallback branch; do not merge or replace stable behavior until build and device tests validate the change.


### 2026-11-18 — Confirm route preservation state and CI retry result

- Re-fetched the active and preserved versions of `fermata/src/auto/java/me/aap/fermata/auto/ProjectionActivity.java`.
- Active `urgent24jam` file blob: `cf1733ee0781d0c8bb3712b4d0efd42f5329f434`. It currently has no `requestRootPermission()` helper and no Root step in the projection startup chain.
- Preserved branch `tqsm-mirror-before-rootless` file blob: `1db8a54bda1a4e422ba6d14666ca0ec22bc6180e`. It retains the original `requestRootPermission()` helper and startup step.
- This confirms the old route is preserved in the backup branch, but is not yet present as an optional route in the active branch. Do not treat the backup branch alone as the final implementation.
- Build #51 run `44226613562`, retry attempt 2, completed with conclusion `cancelled`. Its APK build job was cancelled; no successful APK verification or upload is confirmed.
- No source code or logo assets were changed in this review.
- Next: design an additive implementation that keeps the original Root startup route available as an option alongside the newer route. Before writing code, inspect the current permission and preference architecture; do not overwrite the active file wholesale from the backup branch, because that could roll back unrelated changes.
