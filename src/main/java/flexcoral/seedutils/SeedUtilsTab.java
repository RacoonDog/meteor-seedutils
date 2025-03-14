/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package flexcoral.seedutils;

import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.Feature;
import com.seedfinding.mcfeature.structure.*;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.input.WDropdown;
import meteordevelopment.meteorclient.gui.widgets.input.WIntEdit;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
            WTable table = section.add(theme.table()).widget();

            table.add(theme.label("Structure"));
            table.add(theme.label("Chunk X"));
            table.add(theme.label("Chunk Z"));
            table.add(theme.label("Rm")).widget().tooltip = "Remove";
            table.row();

            SeedUtilsSystem sys = SeedUtilsSystem.get();
            var structs = sys.getAllStructureData();

            for (var u : structs.keySet().stream().sorted().toList()) {
                var d = structs.get(u);

                var l = theme.label(d.name);
                l.tooltip = String.format("UUID: %s\nSalt: %d\nSpacing: %d\nSeparation: %d", u.toString(), d.salt, d.spacing, d.separation);
                table.add(l);

                WIntEdit cxe = theme.intEdit(d.chunkX, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                cxe.action = () -> sys.setStructureData(u, d);
                table.add(cxe);

                WIntEdit cze = theme.intEdit(d.chunkZ, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                cze.action = () -> sys.setStructureData(u, d);
                table.add(cze);

                table.add(theme.minus()).widget().action = () -> {
                    sys.removeStructureData(d);
                    reload();
                };
                table.row();
            }

            WHorizontalList addOpts = theme.horizontalList();
            section.add(addOpts);

            String[] defaultStructureNames = {"buried_treasure", "desert_pyramid", "end_city", "igloo", "jungle_pyramid", "monument", "pillager_outpost", "shipwreck", "swamp_hut"};
            WDropdown<String> addStructType = addOpts.add(theme.dropdown(defaultStructureNames, "desert_pyramid")).widget();
            var addX = addOpts.add(theme.intEdit(0, Integer.MIN_VALUE, Integer.MAX_VALUE, true)).widget();
            var addZ = addOpts.add(theme.intEdit(0, Integer.MIN_VALUE, Integer.MAX_VALUE, true)).widget();
            WDropdown<MCVersion> addVer = addOpts.add(theme.dropdown(MCVersion.values(), MCVersion.v1_21)).widget();
            WButton addBtn = addOpts.add(theme.button("Add structure")).widget();
            addBtn.action = () -> {
                var s = defaultStructures.get(addStructType.get()).create(addVer.get());
                sys.addStructureData(new StructureLifting.Data((UniformStructure<?>) s, addX.get(), addZ.get()));
                reload();
            };
        }

        public void fillLiftingSection(GuiTheme theme, WSection section) {
            WButton startBtn = section.add(theme.button("Start lifting")).expandX().widget();
            startBtn.action = () -> {

                startBtn.action = null; //fuck you

                StructureLifting.Progress progressListener = progress -> MinecraftClient.getInstance().execute(() -> {
                    if (Utils.canUpdate()) {
                        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal("Seed lifting is ")
                            .append(Text.literal(String.format("%.1f%%", progress * 100)).formatted(Formatting.GREEN))
                            .append(" done."), false);
                    }
                });

                CompletableFuture<long[]> seedFuture = StructureLifting.crack(SeedUtilsSystem.get().getAllStructureData().values().stream().toList(), MCVersion.v1_21, progressListener);

                seedFuture.thenAcceptAsync(seeds -> {
                    if (Utils.canUpdate()) {
                        if (seeds.length == 0) {
                            ChatUtils.infoPrefix("SeedUtils", "Seed cracking finished no seeds found.");
                        } else {
                            ChatUtils.infoPrefix("SeedUtils", "Seed cracking finished with (highlight)" + seeds.length + "(default) seeds:");
                            for (int i = 0; i < Math.min(5, seeds.length); i++) {
                                ChatUtils.info("- (highlight)" + seeds[i]);
                            }
                            if (seeds.length > 5) {
                                ChatUtils.info("And " + (seeds.length - 5) + " more.");
                            }
                        }
                    }
                }, MinecraftClient.getInstance());
            };
        }

        public static Map<String, FeatureFactory<? extends Structure<?, ?>>> defaultStructures = new HashMap<>();

        static {
            defaultStructures.put("buried_treasure", BuriedTreasure::new);
            defaultStructures.put("desert_pyramid", DesertPyramid::new);
            defaultStructures.put("end_city", EndCity::new);
            defaultStructures.put("igloo", Igloo::new);
            defaultStructures.put("jungle_pyramid", JunglePyramid::new);
            defaultStructures.put("monument", Monument::new);
            defaultStructures.put("pillager_outpost", PillagerOutpost::new);
            defaultStructures.put("shipwreck", Shipwreck::new);
            defaultStructures.put("swamp_hut", SwampHut::new);
        }

        interface FeatureFactory<T extends Feature<?, ?>> {
            T create(MCVersion version);
        }
    }
}
