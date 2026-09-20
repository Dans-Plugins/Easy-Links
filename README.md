# Easy Links

## Description
Easy Links is a Spigot plugin that lets server administrators save named URLs so that players can look them up in-game with a short command, instead of having long links pasted into chat. Every successful lookup is counted, and `/el stats` reports how many links exist, how often they have been viewed in total, and which one is the most popular.

Links are stored in `plugins/EasyLinks/links.json`. They are written there as soon as they are created or deleted, and their use counts are written when the server shuts down, so both survive a restart.

## Installation
1) Download the latest `EasyLinks-<version>.jar` from [SpigotMC](https://www.spigotmc.org/resources/easylinks-early-access.98040/) or the [Releases](https://github.com/Dans-Plugins/Easy-Links/releases) page.
2) Once downloaded, place the jar in the plugins folder of your server files.
3) Restart your server.

A `config.yml` is generated in `plugins/EasyLinks/` on first run. See the [Configuration Guide](CONFIG.md) for the options it contains.

## Usage
An operator saves a link, and anyone can then look it up:

    /el create "discord" "https://discord.gg/xXtuAQ2"
    /el view "discord"
    /el list

Link names and URLs must be wrapped in double quotes. Without them the command is rejected with its usage message.

Creating and deleting links is restricted to operators by default (`el.create` and `el.delete`); viewing, listing and statistics are available to everyone.

### Documentation
- [User Guide](USER_GUIDE.md) - Installation, getting started and permissions
- [Commands Reference](COMMANDS.md) - Complete list of all commands
- [Configuration Guide](CONFIG.md) - Config options
- [Changelog](CHANGELOG.md) - Notable changes in each release

## Support
You can find the support discord server [here](https://discord.gg/xXtuAQ2).

### Experiencing a bug?
Please open an issue [here](https://github.com/Dans-Plugins/Easy-Links/issues).

## Usage reporting

Usage reporting is on by default: each time the plugin is enabled, and each time one of its commands is run, it sends its name, its version and the command's name to the author's trace server at https://trace.danielstephenson.dev, so it is known which plugins are actually in use. Nothing about players, worlds, IP addresses or the server is sent, and nothing typed after a command is.

To turn it off:

- for this plugin: set `usage-reporting.enabled: false` in `plugins/EasyLinks/config.yml`
- for every plugin on the server that reports to trace: set `enabled: false` in `plugins/trace/config.yml` (written on first start)
- for the whole server process: set the environment variable `TRACE_USAGE_REPORTING=off` (or `DO_NOT_TRACK=1`)

The plugin says on every start whether reporting is on. Details: https://github.com/Stephenson-Software/trace#usage-reporting

## Contributing
- [CONTRIBUTING.md](CONTRIBUTING.md)
