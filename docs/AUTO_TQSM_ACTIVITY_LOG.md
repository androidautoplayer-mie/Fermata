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

### 2026-11-18 — Dispatcher and permission architecture audit (read-only)

- Re-inspected the active urgent24jam sources: ProjectionActivity.java, ProjectionService.java, EventDispatcher.java, MirrorDisplay.java, AccessibilityEventDispatcherService.java, Su.java, fermata/src/auto/AndroidManifest.xml, and accessibility.xml.
- Confirmed screen display is established through the Android MediaProjection consent flow in ProjectionActivity and the foreground ProjectionService.
- Confirmed touch dispatch selection: multiple windows select Accessibility; otherwise the app uses the in-app Activity dispatcher when a supported Fermata activity is active, and falls back to Xposed/Accessibility when it is not.
- Confirmed Accessibility is also used for gesture and multi-window cases. The Root dispatcher remains in EventDispatcher as a fallback when Root has been initialized; the previous Root startup helper was removed from the active branch by the rootless experiment.
- Confirmed Su.get() invokes the Android su process and therefore only applies where a rooted device and its authorization flow support it. This is not a general non-root solution.
- The auto module manifest declares the Accessibility service and Xposed service; the accessibility metadata declares gesture and window-content capabilities.
- The repository has an existing preference/settings framework, but no dedicated Mirror route-selection preference or setup assistant was identified in the files reviewed so far.
- No application source, YouTube/Browser code, or logo assets were changed in this audit.
- No build was run in this step. Physical-device testing remains outstanding.
- Next: inspect the Mirror entry points and UI conventions before adding any optional Root route selection or capability status. Keep current dispatch behavior intact; do not claim a universal touch-control route.

### 2026-11-18 — Confirm original Accessibility route must remain intact

- Re-fetched the active Mirror source files and confirmed the current dispatcher flow in `EventDispatcher.get()`: multi-window sessions use Accessibility; otherwise an active Fermata activity uses the in-app Activity dispatcher; when no active Fermata activity is available, the existing Xposed dispatcher is selected, which falls back to Accessibility when Xposed cannot dispatch.
- Confirmed the Accessibility service remains declared in the auto manifest and its metadata retains gesture dispatch, multi-finger gesture, window-content retrieval, and interactive-window capabilities.
- Confirmed Accessibility also remains the fallback for Back/Home/tap/motion paths and the multi-window path. These are existing behaviors and must not be removed, bypassed, or replaced by a new route.
- User clarified explicitly: preserve the original Accessibility implementation and behavior. Any future route or capability work must be additive and must leave Accessibility intact.
- No application source, manifest, settings, YouTube/Browser code, or logo assets were changed in this inspection.
- No build or physical-device test was run in this step. The earlier CI attempts remain cancelled; no successful APK is verified.
- Next: inspect the Mirror settings/entry-point conventions and any existing Root initialization call sites before designing additive status or optional-route handling. Do not alter dispatcher selection until the full call-site/lifecycle picture is verified.


### 2026-11-18 — Mirror lifecycle and existing settings review (read-only)

- Re-fetched the active branch sources for `MirrorActivity.java`, `MirrorService.java`, `ProjectionActivity.java`, `MirrorDisplay.java`, `EventDispatcher.java`, `AccessibilityEventDispatcherService.java`, `XposedEventDispatcherService.java`, `Su.java`, the auto manifest, `accessibility.xml`, and `MainActivityPrefs.java`.
- Confirmed `MirrorActivity` owns a `MirrorDisplay` reference, forwards head-unit touch input to `MirrorDisplay.motionEvent()`, updates/releases the surface across surface and activity lifecycle callbacks, and releases its display reference on destroy.
- Confirmed `MirrorService` also acquires/releases a `MirrorDisplay` reference. `MirrorDisplay` uses a reference counter and shared weak reference, manages MediaProjection/VirtualDisplay session lifecycle, overlay, wake lock, rotation/brightness restoration, and Xposed service start/stop. Any future status UI must not interfere with these lifecycle paths.
- Confirmed `ProjectionActivity` still uses sequential user-facing Android permission flows for overlay, write-settings, and MediaProjection consent; no automatic permission grant was introduced.
- Confirmed `EventDispatcher.get()` chooses Accessibility for multi-window, the in-app Activity dispatcher when available, otherwise the existing Xposed dispatcher with Accessibility fallback. The Accessibility dispatcher retains its Root dispatcher fallback only when a Root session has already been initialized.
- Confirmed `MainActivityPrefs` provides a shared preference framework, but no Mirror-specific route preference was found in the reviewed preference interface. No route-selection preference or new UI was added.
- No application source, manifest, settings, YouTube/Browser code, or logo assets were changed in this review.
- No build or physical-device test was run. Previous CI attempts remain cancelled; no successful APK is verified.
- Next: inspect the call sites that initialize `EventDispatcher.useSu()` and the Mirror launch/settings entry points on the active branch. Only after that should a minimal, read-only capability/status design be proposed. Preserve all existing dispatcher behavior.


### 2026-11-18 — Mirror entry and Root-dispatch call-site review (read-only)

- Re-fetched active-branch `ProjectionActivity.java`, `ProjectionService.java`, `MirrorService.java`, `MirrorDisplay.java`, `LauncherActivity.java`, `EventDispatcher.java`, and the existing Accessibility/Xposed dispatcher services.
- Confirmed `ProjectionActivity` performs the sequential overlay, write-settings, and official MediaProjection consent flow. `ProjectionService` starts the foreground media-projection service only after receiving the user-approved capture intent.
- Confirmed `MirrorService` and `MirrorActivity` acquire/release the shared `MirrorDisplay`; `MirrorDisplay` creates/manages the VirtualDisplay session, forwards one- and multi-pointer head-unit touch events to the existing `EventDispatcher`, and starts/stops the Xposed dispatcher service as part of its lifecycle.
- Confirmed `LauncherActivity` is the Mirror app-launcher UI and launches selected apps with the existing mirror-mode intent extra. No new Mirror settings/route selector was added in this review.
- `EventDispatcher.useSu(Su)` appears as a method definition in the inspected active-branch dispatcher file. No call site was found in the Mirror files inspected. Repository-wide call-site confirmation remains incomplete because the available GitHub code search did not return indexed results for the active branch; do not infer that no call site exists elsewhere.
- No source code, manifest, YouTube/Browser code, or logo assets were changed. No build or device test was run. No successful APK is verified.
- Engineering decision: do not change dispatcher selection or add a capability UI yet. First obtain a reliable repository-wide call-site inventory (for example, a complete checkout/code search) and verify the exact Mirror entry-point/settings wiring. Preserve current Accessibility behavior and the existing optional Root/Xposed code paths until that evidence is available.


### 2026-11-18 — AUTO module source and Mirror entry-point inventory (read-only)

- Retrieved the recursive Git tree for the exact active branch `urgent24jam` and enumerated all 16 Java files under `fermata/src/auto/java/`.
- Scanned the AUTO module sources for `EventDispatcher.useSu()`, `Su.get()`, and direct Root-dispatch initialization references. In the inspected AUTO module, `EventDispatcher.useSu(Su)` is present as a method definition in `EventDispatcher.java`; no additional call site was identified in the AUTO Java files inspected. This is not yet a complete repository-wide result because the broader 376-file source inventory could not be completed within the available GitHub tool-call limit.
- Rechecked `fermata/src/auto/AndroidManifest.xml`: Mirror services, `ProjectionActivity`, `LauncherActivity`, `ProjectionService`, and the original Accessibility service are declared. The Accessibility service remains protected by Android's `BIND_ACCESSIBILITY_SERVICE` permission.
- Rechecked `MainActivity.java` and `SettingsFragment.java` for Mirror/Projection entry wiring. The inspected settings references concern existing car/mirror activity distinctions; no dedicated Root-vs-non-Root route selector or Mirror capability preference was found in these files.
- No source code, manifest, settings, YouTube/Browser code, or logo assets were changed. No build or physical-device test was run. No successful APK is verified.
- Decision: keep the current dispatcher and Accessibility behavior untouched. The call-site inventory is still explicitly incomplete; do not add route controls or claim Root initialization is unused until a complete checkout-level search is available.


### 2026-11-18 — User-supplied physical-device visual evidence (Fermata Mirror)

- Added a visual-evidence record based on the user's supplied photographs of a phone and Android Auto head unit.
- The photographs show the phone's home screen mirrored on the head-unit display, a browser/web page (LAYARKACA21) visible on both phone and head unit, and the Mirror app-launcher view with installed apps.
- The photographs also show the phone's Android Accessibility settings page with the Fermata Auto service listed as Off, the Fermata Auto "Modify system settings" permission screen with its switch appearing Off, and the head unit displaying "Unlock the phone and grant the requested permissions."
- Evidence interpretation: these photographs document the displayed states only. They do not establish that Accessibility was enabled, that the requested permissions were granted, that touch input from the head unit controlled the phone, or which USB/wireless connection mode was used. Phone model and Android version are not established by the photographs.
- No source code, manifest, settings, YouTube/Browser code, or logo assets were changed in this evidence review. No build was run and no APK is verified by these photographs.
- Next: continue repository-wide Root call-site/lifecycle audit using a complete source inventory where available; separately record a controlled device test for display, touch response, permission state, and connection mode. Preserve the original Accessibility implementation and behavior.


### 2026-11-18 — Recheck previous CI attempt and code-search scope

- Rechecked Build #51 / run `44226613562`: GitHub reports job `134541852626` (`Build Fermata Auto APKs`) as completed with conclusion `cancelled`.
- Rechecked run artifacts: no artifacts were returned. Therefore this run did not produce a verified APK.
- Repeated GitHub code searches for `EventDispatcher useSu` and `Su.get`; the connector searches the repository's default branch, not the active `urgent24jam` branch. These empty results cannot be used as evidence that call sites are absent from the active branch.
- No source code was changed. The active-branch AUTO module scan previously covered 16 Java files; the repository-wide 376-file scan remains incomplete.
- Next: obtain a complete checkout or branch-aware recursive source inventory before making any Root/dispatcher changes. Keep the active dispatcher, Accessibility behavior, Xposed path, and existing Root fallback unchanged until then.


### 2026-11-18 — Root dispatcher call-chain recheck (read-only)

- Re-fetched the exact active and preserved branch versions of `EventDispatcher.java`, `Su.java`, `AccessibilityEventDispatcherService.java`, `ProjectionActivity.java`, `XposedEventDispatcherService.java`, and `MirrorDisplay.java`.
- Active and backup `EventDispatcher.java` blob SHA: `bbd7567e216cb0512a66a93973cd6b5208d6ec77`; active and backup `Su.java` blob SHA: `4c3d0993fe0443017441e21bc6dfc898611314c1`. These files are identical between the two branches.
- Confirmed dispatcher selection: multi-window uses Accessibility; otherwise an active Fermata Activity uses the in-app Activity dispatcher; with no active Fermata Activity, Xposed is selected. Xposed touch/motion dispatch falls back to Accessibility when Xposed cannot dispatch.
- Confirmed Accessibility Back/Home/Tap/Motion paths try the Accessibility service first and then consult `SuDispatcher.get()` only if Accessibility returns false.
- `SuDispatcher.instance` starts as `completedNull()`; it becomes active only through `EventDispatcher.useSu(Su)`. The method definition remains, but no invocation has been found in the AUTO module files inspected so far.
- `Su.get()` probes/starts the Android `su` process and is usable only where a rooted device and its authorization flow support it. It is not a non-root alternative.
- Retrieved the full recursive path tree for branch `urgent24jam`: tree SHA `c65f8480ad6031402d23d2dc980b9bab4b8b1bdf`, `truncated=false`, 799 tracked blobs and 453 Java files. This proves the path inventory is complete, not that all 453 source contents have been searched.
- GitHub code search remains scoped to the default branch. Therefore a repository-wide branch-aware content scan of all Java sources is still outstanding. Do not claim there are no Root call sites anywhere in the repository.
- No application source, manifest, settings, YouTube/Browser code, or logo assets were changed. No build or physical-device test was run; no APK is verified.
- Next: obtain a complete checkout or branch-aware content search for all 453 Java files (plus other relevant source types). Keep dispatcher, Accessibility, Xposed and existing Root fallback unchanged until that scan is complete.


### 2026-11-18 — Full AUTO Java source Root call-site scan (read-only)

- Retrieved all 16 Java files under `fermata/src/auto/java/` from branch `urgent24jam` by exact blob SHA. All 16 fetched successfully; zero fetch errors.
- Root-related references appeared in only `EventDispatcher.java` and `Su.java`.
- `EventDispatcher.useSu(Su)` is a definition at lines 29-30. `SuDispatcher.get()` is used at lines 194, 201, 208, 220 and 228 as a fallback after Accessibility dispatch fails. `SuDispatcher.instance` begins as `completedNull()` at line 234. No caller of `useSu()` was found in the 16 AUTO Java files.
- `Su.get()` is defined at line 28 of `Su.java`; it creates `new Su(...) ` after a successful `su -c ls` probe (line 35). No caller of `Su.get()` was found in the 16 AUTO Java files.
- Confirmed scoped conclusion: Root fallback code exists, but no activation call site was found within the AUTO Java source directory. Do not generalize this to the entire repository yet.
- Dispatcher selection and Accessibility-first fallback behavior remain unchanged. No application code, manifest, settings, YouTube/Browser code, or logo assets changed. No build or physical-device test was run; no APK verified.
- Next: inspect other repository source sets for direct calls, imports, reflection, or integration hooks that could activate `useSu` or `Su.get`. Preserve current behavior until that cross-source inspection is complete.
