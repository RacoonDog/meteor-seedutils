package flexcoral.seedutils;

import flexcoral.seedutils.mixin.SystemsAccessor;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class SeedUtilsAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Seed utils");
    public static final HudGroup HUD_GROUP = new HudGroup("Seed utils");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");
        Tabs.add(new SeedUtilsTab());
        // Modules
//        Modules.get().add(new StructureCollector());

        // HUD
//        Hud.get().register(HudExample.INFO);

        SystemsAccessor.add(new SeedUtilsSystem());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "flexcoral.seedutils";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("maxsupermanhd", "meteor-seedutils");
    }
}
