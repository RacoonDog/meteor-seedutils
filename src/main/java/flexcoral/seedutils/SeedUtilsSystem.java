package flexcoral.seedutils;

import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.*;

public class SeedUtilsSystem extends System<SeedUtilsSystem> {
    public SeedUtilsSystem() {
        super("Seed utils");
    }
    public static SeedUtilsSystem get() {
        return Systems.get(SeedUtilsSystem.class);
    }

    private final Map<UUID, StructureLifting.Data> structureData = new HashMap<>();

    public Map<UUID, StructureLifting.Data> getAllStructureData() {
        return structureData;
    }

    public void addStructureData(StructureLifting.Data d) {
        var u = UUID.randomUUID();
        for(; structureData.containsKey(u); u = UUID.randomUUID()) {}
        structureData.put(u, d);
    }

    public void setStructureData(UUID u, StructureLifting.Data d) {
        structureData.put(u, d);
    }

    public void removeStructureData(UUID u) {
        structureData.remove(u);
    }

    public void removeStructureData(StructureLifting.Data d) {
        for(UUID u : structureData.keySet()) {
            if(structureData.get(u).equals(d)) {
                structureData.remove(u);
                return;
            }
        }
    }

    public void clearStructureData() {
        structureData.clear();
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();
        NbtCompound structs = new NbtCompound();
        for (var e : structureData.entrySet()) {
            structs.put(e.getKey().toString(), e.getValue().toTag());
        }
        tag.put("structures", structs);
        return tag;
    }
    @Override
    public SeedUtilsSystem fromTag(NbtCompound tag) {
        var structs = tag.getCompound("structures");
        for (var k : structs.getKeys()) {
            structureData.put(UUID.fromString(k), new StructureLifting.Data(structs.getCompound(k)));
        }
        return this;
    }
}
