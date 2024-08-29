package flexcoral.seedutils;

import meteordevelopment.meteorclient.utils.misc.ISerializable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class StructureData implements ISerializable<StructureData> {
    public int chunkX;
    public int chunkZ;
    public StructureTypes type;
    public boolean enabled;

    public StructureData() {
        this.chunkX = 0;
        this.chunkZ = 0;
        this.type = StructureTypes.SwampHut;
        this.enabled = false;
    }

    public StructureData(int chunkX, int chunkZ, StructureTypes type, boolean enabled) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.type = type;
        this.enabled = enabled;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();
        tag.putString("type", this.type.name());
        tag.putInt("chunkX", this.chunkX);
        tag.putInt("chunkZ", this.chunkZ);
        tag.putBoolean("enabled", this.enabled);
        return tag;
    }

    @Override
    public StructureData fromTag(NbtCompound tag) {
        this.type = StructureTypes.valueOf(tag.getString("type"));
        this.chunkX = tag.getInt("chunkX");
        this.chunkZ = tag.getInt("chunkZ");
        this.enabled = tag.getBoolean("enabled");
        return this;
    }
}
