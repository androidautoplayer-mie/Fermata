# AUTO-TQSM V2 — Accessibility and Xposed Functional Audit 02

**Date:** 2026-11-26  
**Branch:** `auto-tqsm-v2-clean-start`  
**Scope:** Read-only source trace of Accessibility, Xposed and their relationship to the Android Auto core and Mirror path.

## Executive finding

Accessibility and Xposed are **not shown by this source trace to be prerequisites for the normal Fermata Android Auto media application path** (`CarService` → `MainCarActivity`). They are part of the legacy screen-mirroring input/interaction architecture.

However, the current Mirror startup path is tightly coupled to Accessibility and also requests other high-impact access (overlay, write system settings, root/SU and MediaProjection). Therefore simply deleting the declarations or disabling the service would risk breaking Mirror interactions and may interrupt Mirror startup.

**V2 decision for now: PARK both as optional/legacy Mirror dependencies. Do not require users to enable Accessibility or install/activate Xposed for normal app use. Do not remove their code yet.** First isolate the normal Android Auto media path from Mirror, then decide whether Mirror is retained as an explicitly optional advanced function or removed after owner review.

## Source evidence

### 1. Accessibility service

Files inspected:
- `fermata/src/auto/AndroidManifest.xml`
- `fermata/src/auto/res/xml/accessibility.xml`
- `fermata/src/auto/java/me/aap/fermata/auto/AccessibilityEventDispatcherService.java`

The manifest registers `AccessibilityEventDispatcherService` with `BIND_ACCESSIBILITY_SERVICE`.

Its implementation:
- Dispatches global Back and Home actions.
- Dispatches tap, scale and touch/multitouch gestures.
- Tracks application/input-method windows.
- Can search the active window for a configured button label and click it.
- The service configuration requests broad event types, window content retrieval, interactive windows, touch exploration and gesture capability.

This is broad Accessibility access. It is not a harmless ordinary permission and should not be requested without a demonstrated user-facing need.

### 2. Xposed service

Files inspected:
- `fermata/src/auto/AndroidManifest.xml`
- `fermata/src/auto/res/values/arrays.xml`
- `fermata/src/auto/java/me/aap/fermata/auto/XposedEventDispatcherService.java`

The manifest includes Xposed module metadata and an event-dispatcher service. Its declared scope includes:
- `com.google.android.projection.gearhead`
- `com.android.chrome`
- `com.google.android.apps.maps`

The dispatcher exchanges touch/motion and Back events with a registered activity, and reports the configured mirroring mode. The presence of metadata does not itself prove that an Xposed framework/module is installed or active. No source evidence in this trace establishes Xposed as necessary for the normal media app.

### 3. Event dispatch fallback chain

File inspected:
- `fermata/src/auto/java/me/aap/fermata/auto/EventDispatcher.java`

The Mirror event dispatcher selects among:
- Direct activity event dispatch when a Fermata activity is active.
- Xposed event dispatch when that path is available.
- Accessibility gesture dispatch.
- Root/SU shell input dispatch as another fallback.

Accessibility is used for window counting and as a fallback for Back/Home/tap/touch operations. Xposed is an alternate event route, not a universal requirement.

### 4. Mirror startup coupling

File inspected:
- `fermata/src/auto/java/me/aap/fermata/auto/ProjectionActivity.java`

The current Mirror capture startup sequence is:
1. Check/request overlay permission.
2. Check/request Write Settings access.
3. Check/request Accessibility service enablement.
4. Attempt root/SU setup and set the `PROJECT_MEDIA` app-op.
5. Request MediaProjection screen-capture consent.

The Accessibility check is in the current sequential startup chain. The source therefore confirms that the existing Mirror startup flow deliberately sends users to Accessibility settings if it is not enabled. This is a property of the current Mirror implementation, not proof that all AUTO•TQSM V2 functions require Accessibility.

The root/SU step is also coupled to this legacy path. Its failure behavior and necessity need separate testing before any removal.

### 5. Normal Android Auto path

Files inspected:
- `fermata/src/auto/java/me/aap/fermata/auto/CarService.java`
- `fermata/src/auto/java/me/aap/fermata/auto/MainCarActivity.java`
- `fermata/src/auto/java/me/aap/fermata/auto/MirrorService.java`
- `fermata/src/auto/java/me/aap/fermata/auto/MirrorServiceFS.java`

The normal `CarService` creates `MainCarActivity` and manages the media-service connection/session. Its implementation does not itself request or call the Accessibility service or Xposed dispatcher.

The separate `MirrorService` and `MirrorServiceFS` create the Mirror display path, which uses `MirrorDisplay` and `EventDispatcher` to forward interaction to the phone display.

This supports a clean architectural boundary:
- Core Android Auto media UI/service: should not require Accessibility or Xposed.
- Optional Mirror/screen-projection feature: currently relies on a chain of special permissions and input-dispatch mechanisms.

## Recommendation for V2

1. **Normal users:** no Accessibility enablement and no Xposed installation/activation should be part of the normal setup flow.
2. **Do not delete yet:** keep the legacy Mirror code isolated while the core Android Auto media path is built and tested.
3. **No automatic permission prompts:** do not launch Accessibility settings, request overlay/write-settings, or attempt root access as part of normal startup.
4. **Mirror decision deferred:** later either (a) redesign Mirror with an explicitly optional, transparent permission flow and test it independently, or (b) remove Mirror and its related special-access requirements if the owner decides Mirror is outside the final product.
5. **Xposed:** do not require or recommend an Xposed framework for V2 core functionality. Keep the existing dispatcher parked until Mirror redesign proves it is needed.
6. **Accessibility:** do not expose a broad Accessibility service to normal users merely to preserve a legacy Mirror fallback. If Mirror is retained, investigate narrower, safer alternatives and document exactly what still needs Accessibility.
7. Verify the merged release manifest and runtime flow after architectural separation. A manifest declaration alone is not a runtime requirement, but the current Mirror flow explicitly checks for it.

## Limitations / not yet verified

- This is a source-level audit, not a device test.
- No code, manifest, permission or service declaration was changed in this audit.
- It does not prove that the current APK works without Accessibility/Xposed; the current Mirror startup explicitly expects Accessibility.
- It does not prove the full Android Auto core works on all Android 10–16 devices.
- Root/SU behavior, app-op requirements, MediaProjection restrictions and actual Mirror operation require independent tests.
- No APK build or runtime test was performed.

## Status

**Decision recorded:** Accessibility and Xposed are parked as legacy/optional Mirror dependencies, not normal-user prerequisites for V2 core. Source removal is deferred until the Mirror boundary and test results are established.
