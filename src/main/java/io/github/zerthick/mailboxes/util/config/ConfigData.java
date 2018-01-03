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

package io.github.zerthick.mailboxes.util.config;

import java.util.Collection;

public class ConfigData {

    private final double packagePrice;
    private final Collection<String> mailBoxBlocks;

    public ConfigData(double packagePrice, Collection<String> mailBoxItemIDs) {
        this.packagePrice = packagePrice;
        this.mailBoxBlocks = mailBoxItemIDs;
    }

    public double getPackagePrice() {
        return packagePrice;
    }

    public Collection<String> getMailBoxBlocks() {
        return mailBoxBlocks;
    }
}
