# Mailboxes

A Simple Minecraft Mailboxes Plugin

Mailboxes allows players to send mail to each other at player-created mailboxes. To begin place a sign on a chest with `[mailbox]` on the second line. Assuming you have the `mailboxes.create` permission, the mailbox will be created! To prepare an item to send in the mail hold the item in your main hand and execute `/mailboxes send package <User>` where `User` is the player you want to send the item to. The item will then be wrapped in a package! To mail your package, right-click with it on any mailbox, to check your mail, right-click on any mailbox while not holding a package. To open a sent package, simply hold it in your hand and right-click!

*Note:* Once a mailbox has been created the sign is optional, feel free to destroy it!

## Economy Support
As of Mailboxes v0.3.0, if an economy plugin is installed you may set a cost to sending items in the mail. The prices are set in the `mailboxes.conf` located in `./config/mailboxes`.  The default price is $100, to not charge players any fee set the price to $0.  Players can also have the appropriate `mailboxes.price.bypass` permission to not be charged.

## Commands
`/mb` - Displays version information about the Mailboxes Plugin. (Aliases: mb, mailboxes, mail)  
`/mb send` - Prepares the item held in your main hand to send as various types of items in the mail. (Aliases: send, wrap)  
`/mb send package <User> [Note]` - Wraps the item currently held in your main hand into a package addressed to `User` with an optional `Note` attached (Notes support format codes!). (Aliases: package)

## Permissions
`mailboxes.use`  
`mailboxes.create`  
`mailboxes.destroy`  
`mailboxes.command.info`  
`mailboxes.command.send.package`  
`mailboxes.price.bypass.package`

## Planned Features
* Send different types of mail, packages, letters, notes

## Support Me
I will **never** charge money for the use of my plugins, however they do require a significant amount of work to maintain and update. If you'd like to show your support and buy me a cup of tea sometime (I don't drink that horrid coffee stuff :P) you can do so [here](https://www.paypal.me/zerthick)
