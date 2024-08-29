package flexcoral.seedutils.modules;

import flexcoral.seedutils.SeedUtilsAddon;
import flexcoral.seedutils.StructureData;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;

public class StructureCollector extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    public StructureCollector() {
        super(SeedUtilsAddon.CATEGORY, "structure-collector", "Logs seen structures");
    }

    public List<StructureData> structures = new ArrayList<>();

    @Override
    public WWidget getWidget(GuiTheme theme) {
        return super.getWidget(theme);
    }

    @Override
    public Module fromTag(NbtCompound tag) {
        structures.clear();
        NbtList l = tag.getList("structures", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < l.size(); i++) {
            structures.add(new StructureData().fromTag(l.getCompound(i)));
        }
        return this;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = super.toTag();
        NbtList l = new NbtList();
        for (StructureData structure : structures) {
            l.add(structure.toTag());
        }
        tag.put("structures", l);
        return tag;
    }

    @Override
    public String getInfoString() {
        return structures.size() + " structures";
    }

    @EventHandler
    private void onRender3d(Render3DEvent event) {
//        // Create & stretch the marker object
//        Box marker = new Box(BlockPos.ORIGIN);
//        marker.stretch(
//            scale.get() * marker.getLengthX(),
//            scale.get() * marker.getLengthY(),
//            scale.get() * marker.getLengthZ()
//        );
//
//        // Render the marker based on the color setting
//        event.renderer.box(marker, color.get(), color.get(), ShapeMode.Both, 0);
    }
}
