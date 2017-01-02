# Mailboxes

A Simple Minecraft Mailboxes Plugin

Mailboxes allows players to send mail to each other at player-created mailboxes.To begin place a sign on a chest with `[mailbox]` on the second line. Assuming you have the `mailboxes.create` permission, the mailbox will be created! To prepare an item to send in the mail hold the item in your main hand and execute `/mailboxes send package <User>` where `User` is the player you want to send the item to. The item will then be wrapped in a package! To mail you package, right-click with it on any mailbox, to check your mail, right-click on any mailbox while not holding a package. To open a sent package, simply hold it in your hand and right-click!

## Commands
```
/mb - Displays version information about the Mailboxes Plugin. (Aliases: mb, mailboxes)
/mb send - Prepares the item held in your main hand to send as various types of items in the mail. (Aliases: send, wrap)
/mb send package <User> [Note] - Wraps the item currently held in your main hand into a package addressed to User with an optional Note attached (Notes support format codes!). (Aliases: package)
```

## Permissions
```
mailboxes.create
mailboxes.destroy
mailboxes.command.info
mailboxes.command.send.package
```

## Planned Features
* Send different types of mail, packages, letters, notes
* Economy integration, add cost to sending items in the mail 
