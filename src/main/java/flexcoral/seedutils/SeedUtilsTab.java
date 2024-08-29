/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package flexcoral.seedutils;

import flexcoral.seedutils.modules.StructureCollector;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.gui.widgets.pressable.WMinus;
import meteordevelopment.meteorclient.gui.widgets.pressable.WPlus;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class SeedUtilsTab extends Tab {
    public SeedUtilsTab() {
        super("SeedUtils");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new SeedUtilsScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof SeedUtilsScreen;
    }

    private static class SeedUtilsScreen extends WindowTabScreen {
        public SeedUtilsScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);
        }

        @Override
        public void initWidgets() {
            WVerticalList list = theme.verticalList();

            fillStructuresSection(theme, list.add(theme.section("Structures")).expandX().widget());

            fillLiftingSection(theme, list.add(theme.section("Lifting")).expandX().widget());

            add(list);
        }

        public void fillStructuresSection(GuiTheme theme, WSection section) {
            StructureCollector structureCollector = Modules.get().get(StructureCollector.class);

            WTable table = section.add(theme.table()).expandX().widget();

            table.add(theme.label("Structure type"));
            table.add(theme.label("ChunkX"));
            table.add(theme.label("ChunkZ"));
            table.add(theme.label("En")).widget().tooltip = "Enabled?";
            table.row();

            for (StructureData s : structureCollector.structures) {
                table.add(theme.dropdown(StructureTypes.values(), s.type));
                table.add(theme.intEdit(s.chunkX, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
                table.add(theme.intEdit(s.chunkZ, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
                table.add(theme.checkbox(s.enabled));
                table.row();
            }

            WButton addBtn = section.add(theme.button("Add structure")).expandX().widget();
            addBtn.action = () -> {
                structureCollector.structures.add(new StructureData(0, 0, StructureTypes.SwampHut, true));
                reload();
            };


        }

        public void fillLiftingSection(GuiTheme theme, WSection section) {
            WButton startBtn = section.add(theme.button("Start lifting")).expandX().widget();
            startBtn.action = () -> {
                List<StructureChecker> checkers = new ArrayList<>();
                // w/o pillars 0, 1048575
                // with pillars 0, 65536

                MeteorExecutor.execute(() -> {
                    SeedCrackerTask task = new SeedCrackerTask(checkers, null, null, new ArrayList<>(), 0, 1048575);
                    task.compute();
                    System.out.println("Finished");
                });
            };
        }

//        @Override
//        public boolean toClipboard() {
//            return NbtUtils.toClipboard();
//        }
//
//        @Override
//        public boolean fromClipboard() {
//            return NbtUtils.fromClipboard();
//        }
    }
}
