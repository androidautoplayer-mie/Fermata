# AUTO•TQSM — Session Log
Date: 2026-11-18 (Malaysia time)
Repository: https://github.com/androidautoplayer-mie/Fermata
Branch: urgent24jam
Project: AUTO•TQSM only

## User-reported head-unit status
- On 2026-11-18, the project owner stated that AUTO•TQSM is now visible on their Android Auto car player.
- This is recorded as a user report. The exact launcher entry, screen reached, and working features were not described in this message.
- Do not infer from visibility alone that YouTube playback, Browser UI, or Mirror projection is working. Record those separately only when directly tested and evidenced.

## Source files inspected in this session
- modules/web/build.gradle
- modules/web/src/main/java/me/aap/fermata/addon/web/yt/YoutubeAddon.java
- modules/web/src/main/java/me/aap/fermata/addon/web/yt/YoutubeMediaEngine.java
- fermata/src/main/java/me/aap/fermata/addon/MediaLibAddon.java

## Confirmed source observations
- The :web module registers one YoutubeAddon and one WebBrowserAddon.
- YoutubeAddon extends WebBrowserAddon.
- YoutubeMediaEngine.start() calls web.play().
- YoutubeMediaEngine.YoutubeItem.isVideo() returns true.
- YoutubeAddon contains video-oriented preferences and saved-video resume behavior.
- MediaLibAddon is the interface used for add-ons that contribute items to the media library. The current YoutubeAddon declaration does not implement MediaLibAddon.
- These source facts do not establish a safe or supported way to expose the existing YouTube video WebView as an Android Auto audio-media item.

## Work performed
- Re-opened AUTO-TQSM-PROJECT-MASTER.md on branch urgent24jam.
- Confirmed repository access through the androidautoplayer-mie GitHub account.
- Inspected the source files listed above.
- No application source code or manifest was modified.
- No APK was built in this session.
- No new runtime test was performed in this session.

## Guardrails retained
- Keep AUTO•TQSM separate from MATAMIERA and AUTO’BMK.
- Do not change project branding or logo without explicit instruction.
- Do not expose video/browser UI while driving or bypass Android Auto safety restrictions.
- Do not claim a feature works based only on source inspection or app visibility.
- Preserve narrow, reversible changes and record each material code change with its commit and CI result.


## Follow-up source-path verification (2026-11-18)

- Retrieved the exact recursive Git tree for branch `urgent24jam` from HEAD `17ca40c88c8bf3e2b06ef73552a42158f98c158d`; this resolved the previously uncertain source paths.
- Confirmed the actual settings implementation is `fermata/src/main/java/me/aap/fermata/ui/fragment/SettingsFragment.java`, and the add-on preference builder is an inner class in that same file (not a separate `AddonPrefsBuilder.java`).
- `SettingsFragment.onViewCreated()` asynchronously obtains the activity delegate, builds the adapter, registers listeners, and attaches the RecyclerView adapter. `cleanUp()` removes listeners and destroys the adapter.
- `addAddons()` creates one preference subsection per configured add-on with settings. `AddonPrefsBuilder.configure()` adds the enable toggle and calls the loaded add-on's `contributeSettings()` only when `AddonManager.getAddon(className)` is non-null. `onAddonChanged()` reconfigures the subsection.
- These paths identify the settings lifecycle and dynamic add-on reconfiguration points, but they do NOT identify the cause of the user's Settings crash. No exception stack trace, crash log, or reproducible test is available; do not patch speculatively.
- Confirmed the media-list click handler in `MediaLibFragment.ListAdapter.onClick()`: non-stream playable items route to `onClick(PlayableItem)`, which calls `a.getBody().playItem(i)`; for video items it returns immediately after refreshing list state. This still does NOT explain the reported playlist insertion, because the exact YouTube search/result surface and event source have not been identified.
- No application source, manifest, logo, or branch configuration was changed. No APK was built and no runtime test was performed in this follow-up.
- Next evidence-led step: trace the actual YouTube result view and its click/long-click listeners, then correlate with a device log for the reported playlist action; separately obtain the Settings crash stack trace before any fix.
