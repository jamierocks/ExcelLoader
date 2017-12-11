/*
 * This file is part of ExcelLoader, licensed under the MIT License (MIT).
 *
 * Copyright (c) Jamie Mansfield <https://www.jamierocks.uk/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.jamiemansfield.excel.launch;

import me.jamiemansfield.excel.SharedConstants;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.List;

/**
 * The client tweaker class for the ExcelLoader modification loader.
 */
public class ExcelLoaderClientTweaker extends ExcelLoaderTweaker {

    @Override
    public void _acceptOptions(final List<String> args, final File gameDir, final File assetsDir,
            final String profile) {
        // Populate the launch arguments with any missing *mandatory* arguments
        if (!this.launchArgs.containsKey("--version")) {
            this.launchArgs.put("--version", SharedConstants.Mc.VERSION);
        }
        if (!this.launchArgs.containsKey("--gameDir")) {
            this.launchArgs.put("--gameDir", this.gameDir.getAbsolutePath());
        }
        if (!this.launchArgs.containsKey("--assetsDir") && assetsDir != null) {
            this.launchArgs.put("--assetsDir", assetsDir.getAbsolutePath());
        }
    }

    @Override
    public String getLaunchTarget() {
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        return "net.minecraft.client.main.Main";
    }

}
