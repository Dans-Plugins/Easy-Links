# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

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
