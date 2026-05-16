# Easy Links User Guide

## What is Easy Links?

Easy Links is a Spigot plugin that lets server administrators save named URLs so that players can quickly look them up in-game. Instead of typing long URLs in chat, players run a simple command to view a saved link.

## Installation

1. Download the latest `EasyLinks-<version>.jar` from the [Releases](https://github.com/Dans-Plugins/Easy-Links/releases) page.
2. Place the JAR in your server's `plugins/` folder.
3. Restart the server.

## Getting Started

1. As an operator, create a link: `/el create discord https://discord.gg/xXtuAQ2`
2. Players can view it: `/el view discord`
3. List all links: `/el list`

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `el.help` | `true` | View the help menu. |
| `el.list` | `true` | List all saved links. |
| `el.view` | `true` | View a link's URL. |
| `el.stats` | `true` | View statistics. |
| `el.create` | `op` | Create a new link. |
| `el.delete` | `op` | Delete a link. |

## Support

Ask questions in the [Discord server](https://discord.gg/xXtuAQ2) or open a [GitHub issue](https://github.com/Dans-Plugins/Easy-Links/issues).
