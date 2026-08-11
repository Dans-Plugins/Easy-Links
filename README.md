# Easy Links

## Description
Easy Links is a Spigot plugin that lets server administrators save named URLs so that players can look them up in-game with a short command, instead of having long links pasted into chat. Every lookup is counted, and `/el stats` reports how many links exist, how often they have been viewed in total, and which one is the most popular.

Links are stored in `plugins/EasyLinks/links.json`, so they survive a restart.

## Installation
1) Download the latest `EasyLinks-<version>.jar` from the [Releases](https://github.com/Dans-Plugins/Easy-Links/releases) page.
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

## Contributing
- [CONTRIBUTING.md](CONTRIBUTING.md)
