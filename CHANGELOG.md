# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Fixed
- `/el delete` now actually removes the link from storage (previously reported success without removing it)
- Registered the missing `el.default` permission node for the base `/el` banner command

## [Initial Release]

### Added
- `/el create`, `/el delete`, `/el list`, `/el view`, `/el stats` commands
- Persistent named link storage
