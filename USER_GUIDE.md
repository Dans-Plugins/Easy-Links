# Easy Links User Guide

## What is Easy Links?

Easy Links is a Spigot plugin that lets server administrators save named URLs so that players can quickly look them up in-game. Instead of typing long URLs in chat, players run a simple command to view a saved link.

## Installation

1. Download the latest `Easy-Links-<version>.jar` from [SpigotMC](https://www.spigotmc.org/resources/easylinks-early-access.98040/) or the [Releases](https://github.com/Dans-Plugins/Easy-Links/releases) page.
2. Place the JAR in your server's `plugins/` folder.
3. Restart the server.

## Getting Started

1. As an operator, create a link: `/el create "discord" "https://discord.gg/xXtuAQ2"`
2. Players can view it: `/el view "discord"`
3. List all links: `/el list`

Link names and URLs must be wrapped in double quotes. Without them the command is rejected with its usage message.

Links are written to `plugins/EasyLinks/links.json` as soon as they are created or deleted, and use counts are written when the server shuts down, so both survive a restart.

If `links.json` cannot be read, for example after a hand edit leaves a typo in it, the plugin logs a warning, starts with no links and leaves the file as it is. While that lasts, nothing is written to the file, so links created or deleted and use counts are not kept, and `/el create` and `/el delete` say so in red instead of confirming in green. Repair or remove the file and restart the server to get back to normal.

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `el.default` | `true` | View the plugin banner (running `/el` with no arguments). |
| `el.help` | `true` | View the help menu. |
| `el.list` | `true` | List all saved links. |
| `el.view` | `true` | View a link's URL. |
| `el.stats` | `true` | View statistics. |
| `el.create` | `op` | Create a new link. |
| `el.delete` | `op` | Delete a link. |

## Support

Ask questions in the [Discord server](https://discord.gg/xXtuAQ2) or open a [GitHub issue](https://github.com/Dans-Plugins/Easy-Links/issues).
