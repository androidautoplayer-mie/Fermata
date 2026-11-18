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
- CI Build #49: [run 44225850452](https://github.com/androidautoplayer-mie/Fermata/actions/runs/44225850452) — cancelled. This is not a successful build result.
- CI Build #50: [run 44226090742](https://github.com/androidautoplayer-mie/Fermata/actions/runs/44226090742) — in progress at the time of this log update; this run was triggered by the activity-log documentation commit, so it tests the branch HEAD including the code change.
- Not complete: compatibility diagnostics and a supported alternative touch-control route for devices where Accessibility cannot be enabled.
- Not verified yet: runtime behavior on physical phones and Android Auto head units.

### 2026-11-18 — Mirror touch-dispatch audit (read-only)

- Reviewed `ProjectionActivity.java`, `ProjectionService.java`, `MirrorService.java`, `MirrorActivity.java`, `EventDispatcher.java`, and `AccessibilityEventDispatcherService.java` on branch `urgent24jam`.
- Confirmed the projection startup chain now requests overlay permission, write-settings permission, and then the official MediaProjection consent. It no longer calls the Root setup step.
- Confirmed Mirror touch input from the head-unit surface is forwarded through `MirrorDisplay.motionEvent()`; dispatcher selection can fall back to Accessibility, while Xposed and Root dispatchers remain in the existing source.
- Important limitation: removing the mandatory Root step does not create a new non-Accessibility touch route. The current source still relies on supported in-app activity dispatch or Accessibility/Xposed/Root paths depending on runtime state.
- No code was changed during this audit. No YouTube, Browser, or unrelated module was edited.
- Next engineering task: inspect `MirrorDisplay` and the exact touch-event coordinate/dispatch path, then design a truthful capability check. Investigate any alternative input path separately before implementing it; do not claim Bluetooth HID or universal touch support without evidence.
- Physical-device testing remains outstanding.

## Next steps

1. Verify Build #50 and record its final result before further code changes.
2. Inspect `MirrorDisplay` touch forwarding and coordinate transformation.
3. Design a compatibility check that distinguishes screen display from touch-control capability.
4. Investigate legitimate supported alternative input routes; do not assume Bluetooth HID works without device/player testing.
5. Add clear in-app status and guidance for unsupported control modes, isolated to Mirror.
6. Test on physical devices and record phone model, Android version, USB/wireless connection, display result, and touch result.

## Ongoing record

Append each meaningful AUTO TQSM project activity below. Keep commit hashes and CI results exact; distinguish code changes, successful builds, and real-device verification.
