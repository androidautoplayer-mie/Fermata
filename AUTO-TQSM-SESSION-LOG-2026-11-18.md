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


## YouTube event-path and CI verification follow-up (2026-11-18)

- Inspected `YoutubeFragment`, `YoutubeWebView.attachListeners()`, `YoutubeJsInterface.handleEvent()`, `YoutubeMediaEngine`, `YoutubeWebClient`, and `WebBrowserFragment.loadUrl()` on `urgent24jam`.
- The YouTube WebView JavaScript bridge currently handles video found/playing/paused/ended, progress, video quality results, and fullscreen events. The listener attaches HTML video element lifecycle handlers; it does not register a click handler for YouTube search-result cards or a playlist-add action.
- `YoutubeMediaEngine.play()` calls the WebView's JavaScript video play method. Its current item is an internal `ExtPlayable` whose `isVideo()` returns true. This remains a video-oriented page playback engine, not an audio-only YouTube media catalog.
- `YoutubeWebClient.shouldOverrideUrlLoading()` routes non-YouTube links to the Browser fragment; `WebBrowserFragment.loadUrl()` routes YouTube URLs back to the YouTube fragment. These navigation routes do not explain a result tap being added to a playlist.
- Source audit still cannot identify the reported playlist-add action. It may occur inside YouTube's own web UI or an Android Auto/media surface, but that is not established. Do not change click behavior without an exact reproduction surface or runtime trace.
- GitHub Actions run `44133520539` for documentation commit `e7fbd9d21ba8e44605b4f9c9560d783a13b57cf7` completed successfully. Job `Build Fermata Auto APKs` succeeded, including APK signature/package verification and uploads. Artifacts: arm64 `14350633148`, arm `14352345077`; both unexpired at time of check. This verifies the current branch source compiles/packages for the AUTO flavor, but the commit contains documentation changes only and does not verify runtime fixes.
- No application source, manifest, logo, or build configuration was changed. No device runtime test was performed.


## Head-unit empty Media Service diagnosis from user evidence (2026-11-18)

- Reviewed the user's four supplied photos. They show: (1) AUTO•TQSM app icon on the phone alongside Fermata Auto and Fermata Mirror; (2) the head-unit Media Service page with Folders/Favorites/Playlists and "No items"; (3) Android Auto phone-side Customize Launcher with "AUTO•TQSM Media Service" enabled; and (4) AUTO•TQSM icon on the head-unit launcher. This proves launcher visibility/service registration, not successful media browsing or YouTube playback on the head unit.
- Inspected `YoutubeAddon.java`: class extends `WebBrowserAddon` and implements `PreferenceStore.Listener`; it does not implement `MediaLibAddon`. `WebBrowserAddon` implements `FermataFragmentAddon` and `SharedPreferenceStore`, not `MediaLibAddon`.
- Inspected `DefaultMediaLib.getChildren(root)`: the media-service root always adds Folders, Favorites, and Playlists, then appends roots only from loaded add-ons implementing `MediaLibAddon`.
- Inspected `AddonManager`: loaded add-ons are filtered by enabled preferences and build flavor; `getAddons(MediaLibAddon.class)` only returns loaded add-ons that implement that interface.
- Strong source-level explanation for the observed empty root: AUTO•TQSM Media Service is registered and exposes the default folders/favorites/playlists containers, but the existing YouTube WebView add-on is not a MediaLibAddon and therefore does not contribute YouTube browse items to the Android Auto media root. This matches the user's photos. This is a source-level diagnosis, not a device-runtime proof; existing containers can also be empty if they contain no configured items.
- Important distinction: YouTube playing in the phone's Fermata WebView does not itself create Android Auto MediaBrowser browse items. Any future media integration must respect Android Auto's supported media categories and driving-safety restrictions; do not expose video playback controls/content as a driving-time loophole.
- No source code, manifest, logo, or branch was changed. No APK was rebuilt in response to these photos. Runtime head-unit test remains unperformed.
