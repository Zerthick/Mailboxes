package io.github.zerthick.mailboxes.cmd.executors;

import io.github.zerthick.mailboxes.Mailboxes;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;

public abstract class AbstractCommandExecutor implements CommandExecutor {

    protected PluginContainer container;
    protected Mailboxes plugin;

    public AbstractCommandExecutor(Mailboxes plugin) {
        this.plugin = plugin;
        this.container = plugin.getInstance();
    }
}
