/*
 * Copyright (C) 2018  Zerthick
 *
 * This file is part of Mailboxes.
 *
 * Mailboxes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Mailboxes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mailboxes.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.mailboxes;

import com.google.inject.Inject;
import io.github.zerthick.mailboxes.cmd.CommandRegister;
import io.github.zerthick.mailboxes.inventory.MailboxInventory;
import io.github.zerthick.mailboxes.inventory.MailboxInventoryManager;
import io.github.zerthick.mailboxes.location.MailboxLocationManager;
import io.github.zerthick.mailboxes.mailbox.data.MailboxData;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.MailItemKeys;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.immutable.ImmutableMailItemData;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.mutable.MailItemData;
import io.github.zerthick.mailboxes.util.DateFormatter;
import io.github.zerthick.mailboxes.util.config.ConfigData;
import io.github.zerthick.mailboxes.util.config.ConfigManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Plugin(
        id = "mailboxes",
        name = "Mailboxes",
        version = "1.0.0",
        description = "A Simple Minecraft Mailboxes Plugin",
        authors = {
                "Zerthick"
        }
)
public class Mailboxes {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer instance;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfigPath;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path defaultConfigDir;

    private MailboxLocationManager mailboxLocationManager;
    private MailboxInventoryManager mailboxInventoryManager;
    private EconomyService economyService;
    private ConfigData configData;

    public Logger getLogger() {
        return logger;
    }

    public PluginContainer getInstance() {
        return instance;
    }

    public Path getDefaultConfigPath() {
        return defaultConfigPath;
    }

    public Path getDefaultConfigDir() {
        return defaultConfigDir;
    }

    @Listener
    public void onServerInit(GameInitializationEvent event) {

        //Register Data Manipulators
        MailboxData.registerData(getInstance());

        //Register Serializers
        ConfigManager.registerSerializers();

        //Create tables if they don't exist
        ConfigManager.createTables(this);

        //Register Commands
        CommandRegister.registerCommands(this);

        mailboxLocationManager = ConfigManager.loadMailboxLocationManager(this);
        mailboxInventoryManager = new MailboxInventoryManager(new HashMap<>());

        //Load config data
        configData = ConfigManager.loadConfigData(this);
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {

        UUID playerUUID = event.getTargetEntity().getUniqueId();

        //Load Inventory from Database
        Sponge.getScheduler().createTaskBuilder().async()
                .execute(() -> {
                    MailboxInventory mailboxInventory = ConfigManager.loadMailboxInventory(playerUUID, this);
                    mailboxInventoryManager.addInventory(playerUUID, mailboxInventory);

                    if (mailboxInventory.isUnread()) {
                        event.getTargetEntity().sendMessage(Text.of(TextColors.GOLD, "You have unread mail!"));
                    }
                }).submit(getInstance());
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect event) {

        UUID playerUUID = event.getTargetEntity().getUniqueId();

        //Save Inventory to Database
        Sponge.getScheduler().createTaskBuilder().async()
                .execute(() -> mailboxInventoryManager.removeInventory(playerUUID).ifPresent(inventory ->
                        ConfigManager.saveMailboxInventory(playerUUID, inventory, this))).submit(getInstance());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Log Start Up to Console
        getLogger().info(
                instance.getName() + " version " + instance.getVersion().orElse("")
                        + " enabled!");
    }


    @Listener
    public void onServerStop(GameStoppedServerEvent event) {

        ConfigManager.saveMailboxLocationManager(mailboxLocationManager, this);
        ConfigManager.saveMailboxInventories(mailboxInventoryManager, this);
    }

    @Listener
    public void onChangeSign(ChangeSignEvent event, @Root Player player, @Getter("getText") SignData data) {

        ListValue<Text> lines = data.lines();

        //Check the second line of the sign
        if (lines.get(1).toPlain().equalsIgnoreCase("[Mailbox]")) {

            Sign sign = event.getTargetTile();
            Location<World> signLocation = sign.getLocation();

            sign.getBlock().get(Keys.DIRECTION).ifPresent(direction -> {
                Location<World> potentialChestLocation = signLocation.getBlockRelative(direction.getOpposite());

                //If the sign is attached to a mailbox block
                if (isMailboxBlock(potentialChestLocation.getBlock().getType())) {

                    if (player.hasPermission("mailboxes.create")) {

                        mailboxLocationManager.addLocation(potentialChestLocation.getExtent().getUniqueId(), potentialChestLocation.getBlockPosition());

                        lines.set(1, Text.of(TextColors.GOLD, "[Mailbox]"));
                        data.set(Keys.SIGN_LINES, lines.get());
                        player.sendMessage(Text.of(TextColors.BLUE, "Mailbox Created!"));
                    } else {
                        player.sendMessage(Text.of(TextColors.RED, "You don't have permission to create Mailboxes!"));
                    }
                }

            });
        }

    }

    @Listener
    public void onBlockChange(ChangeBlockEvent event) {

        //Filter out transactions that are not chests and are not mailbox locations
        event.getTransactions().stream()
                .filter(blockSnapshotTransaction -> isMailboxBlock(blockSnapshotTransaction.getOriginal().getState().getType()))
                .filter(blockSnapshotTransaction -> {
                    Optional<Location<World>> originalLocation = blockSnapshotTransaction.getOriginal().getLocation();
                    return originalLocation.map(worldLocation -> mailboxLocationManager.isLocation(worldLocation.getExtent().getUniqueId(), worldLocation.getBlockPosition())).orElse(false);
                })
                .forEach(blockSnapshotTransaction -> {
                    if (event instanceof ChangeBlockEvent.Break) {
                        if (event.getCause().root() instanceof Player) {
                            Player player = (Player) event.getCause().root();
                            if (player.hasPermission("mailboxes.destroy")) {
                                blockSnapshotTransaction.getOriginal().getLocation().ifPresent(location -> mailboxLocationManager.removeLocation(location.getExtent().getUniqueId(), location.getBlockPosition()));
                                player.sendMessage(Text.of(TextColors.RED, "Mailbox Destroyed!"));
                            } else {
                                player.sendMessage(Text.of(TextColors.RED, "You don't have permission to destroy Mailboxes!"));
                            }
                        } else {
                            blockSnapshotTransaction.setValid(false);
                        }
                    } else {
                        blockSnapshotTransaction.setValid(false);
                    }
                });
    }

    @Listener
    public void onBlockInteract(InteractBlockEvent.Secondary event, @Root Player player, @Getter("getTargetBlock") BlockSnapshot blockSnapshot) {

        if (isMailboxBlock(blockSnapshot.getState().getType())) {
            blockSnapshot.getLocation().ifPresent(location -> {
                if (mailboxLocationManager.isLocation(location.getExtent().getUniqueId(), location.getBlockPosition())) {

                    //Check that the player has permission to use mailboxes
                    if (player.hasPermission("mailboxes.use")) {

                        Optional<ItemStack> itemStackOptional = player.getItemInHand(event.getHandType());

                        //If the player is holding a mail item in their hand
                        if (itemStackOptional.isPresent() && isMailItem(itemStackOptional.get())) {

                            //Economy check
                            if (economyService != null && configData.getPackagePrice() > 0 && !player.hasPermission("mailboxes.price.bypass.package")) {
                                Account playerAccount = economyService.getOrCreateAccount(player.getIdentifier()).get();
                                if (playerAccount.getBalance(economyService.getDefaultCurrency()).compareTo(BigDecimal.valueOf(configData.getPackagePrice())) < 0) {
                                    player.sendMessage(Text.of(TextColors.RED, "You don't have enough funds!"));
                                    event.setCancelled(true);
                                    return;
                                }
                            }

                            ItemStack itemStack = buildSentMailItemStack(itemStackOptional.get());
                            UUID receiver = itemStack.get(MailItemKeys.MAIL_ITEM_RECEIVER).get();

                            InventoryTransactionResult result;

                            Optional<MailboxInventory> inventory = mailboxInventoryManager.getInventory(receiver);
                            if (inventory.isPresent()) {
                                //Player is online, we can add the mail directly
                                result = inventory.get().addMail(itemStack);
                            } else {
                                //Player is offline load the mail inventory, offer the item, and then save it.
                                MailboxInventory offlinePlayerInventory = ConfigManager.loadMailboxInventory(receiver, this);
                                result = offlinePlayerInventory.addMail(itemStack);

                                if (result.getType() == InventoryTransactionResult.Type.SUCCESS) {
                                    ConfigManager.saveMailboxInventory(receiver, offlinePlayerInventory, this);
                                }
                            }

                            //Send message to the sender based on the result of the inventory transaction
                            if (result.getType() == InventoryTransactionResult.Type.SUCCESS) {

                                //Remove player funds if needed
                                if (economyService != null && configData.getPackagePrice() > 0 && !player.hasPermission("mailboxes.price.bypass.package")) {
                                    Account playerAccount = economyService.getOrCreateAccount(player.getIdentifier()).get();
                                    playerAccount.withdraw(economyService.getDefaultCurrency(),
                                            BigDecimal.valueOf(configData.getPackagePrice()),
                                            Cause.of(NamedCause.source(getInstance())));
                                }

                                player.setItemInHand(event.getHandType(), null);
                                player.sendMessage(Text.of(TextColors.GOLD, "Mail Sent!"));

                                //If the receiver is online, send them a message they they got mail
                                Sponge.getServer().getPlayer(receiver).ifPresent(p -> p.sendMessage(Text.of(TextColors.GOLD, "You have mail from ",
                                        TextColors.WHITE, player.getName())));
                            } else if (result.getType() == InventoryTransactionResult.Type.FAILURE) {
                                player.sendMessage(Text.of(TextColors.RED, "Unable to send mail: Receiver Mailbox Full!"));
                            } else {
                                player.sendMessage(Text.of(TextColors.RED, "Unable to send mail: Unknown Error!"));
                            }

                        } else { //The player is not holding a mail item in their hand, open their mail inventory instead
                            mailboxInventoryManager.getInventory(player.getUniqueId()).ifPresent(mailboxInventory -> {
                                mailboxInventory.setUnread(false);
                                player.openInventory(mailboxInventory.getMailInventory(),
                                        Cause.of(NamedCause.source(instance)));
                            });
                        }
                    } else {
                        player.sendMessage(Text.of(TextColors.RED, "You don't have permission to use mailboxes!"));
                    }
                    event.setCancelled(true);
                }
            });
        } else {
            player.getItemInHand(event.getHandType()).ifPresent(itemStack -> event.setCancelled(isMailItem(itemStack)));
        }
    }

    @Listener
    public void onItemInteract(InteractItemEvent.Secondary event, @Root Player player, @Getter("getItemStack") ItemStackSnapshot itemStackSnapshot) {
        if (isMailItem(itemStackSnapshot)) {
            itemStackSnapshot.get(MailItemKeys.MAIL_ITEM_IS_SENT).ifPresent(b -> {
                if (b) {
                    itemStackSnapshot.get(MailItemKeys.MAIL_ITEM_SNAPSHOT).ifPresent(mailItemStackSnapshot -> {
                        player.setItemInHand(event.getHandType(), mailItemStackSnapshot.createStack());
                        event.setCancelled(true);
                    });
                }
            });
        }
    }

    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
        //Hook into economy service
        if (event.getService().equals(EconomyService.class)) {
            economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
        }
    }

    private boolean isMailItem(ItemStack itemStack) {
        return itemStack.get(MailItemData.class).isPresent();
    }

    private boolean isMailItem(ItemStackSnapshot itemStack) {
        return itemStack.get(ImmutableMailItemData.class).isPresent();
    }

    private ItemStack buildSentMailItemStack(ItemStack itemStack) {

        UUID sender = itemStack.get(MailItemKeys.MAIL_ITEM_SENDER).get();

        itemStack.offer(MailItemKeys.MAIL_ITEM_DATE, DateFormatter.formatDate(ZonedDateTime.now()));
        itemStack.offer(MailItemKeys.MAIL_ITEM_IS_SENT, true);

        try {
            itemStack.offer(Keys.DISPLAY_NAME, Text.of("From: ", Sponge.getServer().getGameProfileManager().get(sender).get().getName().get()));
        } catch (InterruptedException | ExecutionException e) {
            //Ignore, this shouldn't happen as the sender had to be a user on the server at some point
        }

        //Build item lore to include date
        List<Text> lore = new ArrayList<>();
        lore.add(Text.of(TextColors.GRAY, itemStack.get(MailItemKeys.MAIL_ITEM_DATE).get()));
        if (itemStack.get(Keys.ITEM_LORE).isPresent()) {
            lore.addAll(itemStack.get(Keys.ITEM_LORE).get());
        }
        itemStack.offer(Keys.ITEM_LORE, lore);

        return itemStack;
    }

    private boolean isMailboxBlock(BlockType blockType) {
        return isMailboxBlock(blockType.getId());
    }

    private boolean isMailboxBlock(String id) {
        return configData.getMailBoxBlocks().contains(id);
    }
}
