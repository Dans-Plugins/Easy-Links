# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Fixed

- The `Dev Release` workflow now retries publishing the `dev` prerelease before giving up. The release and its tag have to be deleted and recreated for the tag to move to the new commit, and a transient API failure inside that window previously left the repository with no `dev` release at all until the workflow was re-run by hand. Each attempt now starts from a clean slate, and an exhausted retry fails loudly.

### Added

- A `README.md` describing what the plugin does, how it is installed, a minimal usage example and where the support channels are, with links onward to the user guide, command reference, configuration guide and contributing guide. It previously read `(TBD)`.
- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `main` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get easylinks --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.

### Fixed

- Links are now actually persisted. `/el create` and `/el delete` write `links.json` immediately, and a shutdown save records the per-link use counts behind `/el stats`. Previously nothing ever called the save routine, so every link and every use count created during a session was discarded on restart.
- The quoting required by `/el create`, `/el delete` and `/el view` is now documented. `COMMANDS.md` and `USER_GUIDE.md` showed unquoted examples such as `/el view discord`, which the argument parser rejects.

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
