package flexcoral.seedutils.modules;

import flexcoral.seedutils.SeedUtilsAddon;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;

public class EndCollector extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    public final Setting<Integer> pillarSeed = sgGeneral.add(new IntSetting.Builder()
        .name("pillar-seed")
        .description("Discovered or inputted pillar seed")
        .defaultValue(0)
        .build()
    );

    public final Setting<Integer> gatewayIndex = sgGeneral.add(new IntSetting.Builder()
        .name("gateway-index")
        .description("Discovered or inputted index of first gateway that generated")
        .defaultValue(0)
        .min(0)
        .max(20)
        .build()
    );

    public EndCollector(Category category, String name, String description) {
        super(SeedUtilsAddon.CATEGORY, "end-collector", "Logs pillar seed and first gateway");
    }
}
