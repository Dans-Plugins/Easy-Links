# Easy Links Commands

All commands use `/el` or `/easylinks` as the base.

Arguments must be wrapped in double quotes. A command given unquoted arguments is rejected with its usage message.

| Command | Description | Permission |
|---------|-------------|------------|
| `/el` | View the plugin banner (name, version, repository link). | `el.default` |
| `/el help` | View a list of commands. | `el.help` |
| `/el list` | List all saved links. | `el.list` |
| `/el view "<name>"` | View the URL for a saved link. | `el.view` |
| `/el stats` | View plugin statistics. | `el.stats` |
| `/el create "<name>" "<url>"` | Create a new link. | `el.create` |
| `/el delete "<name>"` | Delete a link. | `el.delete` |
