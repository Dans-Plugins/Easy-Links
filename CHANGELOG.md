# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

## [0.6.1] – 2026-10-07

### Changed

- The usage-reporting "Details" link (startup notice, `config.yml` and the docs) now points at https://danielstephenson.dev/usage-reporting, a public page; the previous link led to a private repository and returned 404 for everyone. The vendored trace client is now 0.6.1, which carries the same link in the `plugins/trace/config.yml` header it writes. Details: https://github.com/Stephenson-Software/trace-client-java/releases/tag/0.6.1.

## [0.6.0] – 2026-10-06

### Changed

- The vendored trace client is now 0.5.0, and every usage event now carries a random server ID: the `server-id` line the client appends to `plugins/trace/config.yml` on first run, so servers can be counted rather than events. It identifies no person, account or IP address, and deleting the line gets a new one. The startup notice, the `config.yml` comment, `CONFIG.md` and the README now say so; before, they said nothing about the server was sent. The opt-outs are unchanged.

### Added

- Minecraft 26.3 is now a supported version, listed in `minecraft-versions.json` and the README.

## [0.5.0] – 2026-10-02

### Changed

- The vendored trace client is now 0.4.0, and every usage event now carries the plugin version, `command` events included; before, only `startup` did.
- The vendored trace client is now 0.3.0. `plugins/trace/config.yml` can now carry a `tags:` block whose entries are added to every usage event the plugin sends, so a test server can mark its own events (the release gates write `ci: "true"`) and be left out of the figures for real installations. Nothing changes for a server whose `plugins/trace/config.yml` has no `tags:` block.

### Added

- The supported Minecraft versions are now declared in `minecraft-versions.json` — currently 1.19.4, 1.21.11 and 26.2 — and listed in the README. Every build checks that the plugin only uses Bukkit API that exists on each of them.

## [0.4.0] – 2026-09-19

### Fixed

- Subcommands are now dispatched through the command service that was actually given the plugin's commands. The plugin constructed a second `CommandService` of its own and routed `/el help`, `/el list`, `/el view`, `/el stats`, `/el create` and `/el delete` through it, while the command list was handed to the instance Ponder owns; the second instance was never initialized, so dispatching through it could not reach any subcommand. The plugin now dispatches through Ponder's instance and no longer holds one of its own. `/el` with no arguments was unaffected.
- The `el.default` permission is now actually enforced. `/el` with no arguments is dispatched straight from the plugin rather than through Ponder's command service, so the node it declares was never checked and an administrator who negated it for a group still saw that group shown the banner. The same permission check the registered subcommands go through is now applied before the banner is sent, so the refusal reads identically. Nothing changes on a default installation, where the node defaults to `true`.
- The banner shown by `/el` now points at `https://github.com/Dans-Plugins/Easy-Links`. It previously advertised a wiki under `dmccoystephenson/Easy-Links`, which is both the pre-transfer owner and a wiki that has never been created, so players following it reached an empty page.
- The `Dev Release` workflow now retries publishing the `dev` prerelease before giving up. The release and its tag have to be deleted and recreated for the tag to move to the new commit, and a transient API failure inside that window previously left the repository with no `dev` release at all until the workflow was re-run by hand. Each attempt now starts from a clean slate, and an exhausted retry fails loudly.
- Links are now actually persisted. `/el create` and `/el delete` write `links.json` immediately, and a shutdown save records the per-link use counts behind `/el stats`. Previously nothing ever called the save routine, so every link and every use count created during a session was discarded on restart.
- The quoting required by `/el create`, `/el delete` and `/el view` is now documented. `COMMANDS.md` and `USER_GUIDE.md` showed unquoted examples such as `/el view discord`, which the argument parser rejects.

### Changed

- Usage reporting is now disclosed on every start: the console says whether it is on, what is sent and where, and how to turn it off, or why it is off. A server-wide switch is added — `enabled: false` in `plugins/trace/config.yml` (written by the first reporting plugin to start) turns reporting off for every plugin that reports to trace — as are the environment variables `TRACE_USAGE_REPORTING=off` and `DO_NOT_TRACK=1`. The `usage-reporting` block is now written into `plugins/EasyLinks/config.yml` the first time the plugin starts without it, rather than on the next version change, so the opt-out is visible on upgraded servers too. What is sent is unchanged. Details: https://github.com/Stephenson-Software/trace#usage-reporting

### Added

- The plugin now reports usage events — `startup` on enable, `command` on each of its commands — to the author's trace server so it is known which plugins are in use. Events carry the plugin name, the event name, and the plugin version or command name; nothing about players or the server. Reporting runs off the main thread, never delays a tick, drops silently when the server is unreachable, and is turned off with `usage-reporting.enabled: false` in `config.yml`. The default config carries the plugin's key, so reporting is active out of the box unless turned off — including on servers upgraded from a version before the `usage-reporting` block existed, whose `config.yml` is not rewritten until the plugin version changes: the plugin reads the bundled defaults for any key the file lacks. A bundled `config.yml` is now shipped and written out on first run, alongside the `version` and `debugMode` entries the plugin already generated.
- A `README.md` describing what the plugin does, how it is installed, a minimal usage example and where the support channels are, with links onward to the user guide, command reference, configuration guide and contributing guide. It previously read `(TBD)`.
- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `main` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get easylinks --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.

## [0.4.0-SNAPSHOT-8-8-2026] – 2026-08-08

### Changed
- Easy-Links is now developed AI-first. Day-to-day feature work, grooming, review and maintenance run through AI agents working directly against this repository, with the maintainers setting direction and approving what lands. The version bump marks that change in how the project is built — it is not a break in behaviour, configuration or stored data, and existing installations can upgrade in place. Released as `0.4.0-SNAPSHOT-8-8-2026`: the AI-first line has not yet been verified in live operation, and the dated snapshot designation stays until it has.

### Added
- `/el stats` now reports real usage data: total uses is summed from actual per-link use counts, and the most popular link is the one with the most uses (`/el view` increments a link's use count on each successful lookup)
- Debug logging to the console, gated on the `debugMode` config option, at config-option-set and link storage load/save points

### Fixed
- `/el delete` now actually removes the link from storage (previously reported success without removing it)
- Registered the missing `el.default` permission node for the base `/el` banner command

## [Initial Release]

### Added
- `/el create`, `/el delete`, `/el list`, `/el view`, `/el stats` commands
- Persistent named link storage
