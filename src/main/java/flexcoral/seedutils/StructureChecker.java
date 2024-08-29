package flexcoral.seedutils;

import java.util.List;

public class StructureChecker {
    public int salt;
    public StructureTypes type;
    public List<int[]> coordinates;

    public StructureChecker(int salt, StructureTypes type, List<int[]> coordinates) {
        this.salt = salt;
        this.type = type;
        this.coordinates = coordinates;
    }

    public boolean canGenerateAtAll(long halfSeed) {
        for (int[] coord : coordinates) {
            if (!canGenerateInTheFirstPlace(halfSeed, coord[0], coord[1])) {
                return false;
            }
        }
        return true;
    }

    private boolean canGenerateInTheFirstPlace(long halfSeed, int chunkX, int chunkZ) {
        int regX;
        int regZ;
        switch (this.type) {
            case Shipwreck:
                regX = Math.floorDiv(chunkX , 24);
                regZ = Math.floorDiv(chunkZ, 24);
                break;
            case Mansion:
                regX = Math.floorDiv(chunkX , 80);
                regZ = Math.floorDiv(chunkZ, 80);
                break;
            case TrialChamber:
                regX = Math.floorDiv(chunkX , 34);
                regZ = Math.floorDiv(chunkZ, 34);
                break;
            case OceanRuin:
                regX = Math.floorDiv(chunkX , 20);
                regZ = Math.floorDiv(chunkZ, 20);
                break;
            default:
                regX = chunkX >> 5;
                regZ = chunkZ >> 5;
        }
        long mask = switch (this.type) {
            case Shipwreck -> 3;
            case Mansion -> 3;
            case TrialChamber -> 1;
            case OceanRuin -> 1;
            default -> 7;
        };
        long newHalfSeed = ((halfSeed + regX * 341873128712L + regZ * 132897987541L + salt) ^ 25214903917L) & ((1L << 32) - 1);
        newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
        if (this.type == StructureTypes.Mansion) {
            long x1 = (newHalfSeed >> 17) & mask;
            newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
            long x2 = (newHalfSeed >> 17) & mask;
            long x = (x1 + x2) / 2;
            if (x == (chunkX & mask)) {
                newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
                long z1 = (newHalfSeed >> 17) & mask;
                newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
                long z2 = (newHalfSeed >> 17) & mask;
                long z = (z1 + z2) / 2;
                return z == (chunkZ & mask);
            }
        }
        if (((newHalfSeed >> 17) & mask) == (chunkX & mask)) {
            newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
            return ((newHalfSeed >> 17) & mask) == (chunkZ & mask);
        }
        return false;
    }

    public boolean canActuallyGenerate(long fullSeed) {
        for (int[] coord : coordinates) {
            if (!canActuallyGenerate(fullSeed, coord[0], coord[1])) {
                return false;
            }
        }
        return true;
    }

    public boolean canActuallyGenerate(long fullSeed, int chunkX, int chunkZ) {
        if (this.type == StructureTypes.BuriedTreasure) {
            long newFullSeed = ((fullSeed + chunkX * 341873128712L + chunkZ * 132897987541L + salt) ^ 25214903917L) & ((1L << 48) - 1);
            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
            long next24 = newFullSeed>>24;
            if(next24<1677216.16){
                return true;
            }
            return false;
        }
        int regX;
        int regZ;
        switch (this.type) {
            case RuinedPortal:
                regX = Math.floorDiv(chunkX , 40);
                regZ = Math.floorDiv(chunkZ, 40);
                break;
            case Shipwreck, AncientCity:
                regX = Math.floorDiv(chunkX , 24);
                regZ = Math.floorDiv(chunkZ, 24);
                break;
            case Mansion:
                regX = Math.floorDiv(chunkX , 80);
                regZ = Math.floorDiv(chunkZ, 80);
                break;
            case TrialChamber, Village:
                regX = Math.floorDiv(chunkX , 34);
                regZ = Math.floorDiv(chunkZ, 34);
                break;
            case OceanRuin:
                regX = Math.floorDiv(chunkX , 20);
                regZ = Math.floorDiv(chunkZ, 20);
                break;
            case OceanMonument:
                regX = Math.floorDiv(chunkX , 32);
                regZ = Math.floorDiv(chunkZ, 32);
                break;
            default:
                regX = chunkX >> 5;
                regZ = chunkZ >> 5;
        }
        long mask = switch (this.type) {
            case RuinedPortal -> 25;
            case Shipwreck -> 20;
            case TrialChamber -> 22;
            case Village -> 26;
            case OceanRuin -> 12; // 27 ???
            default -> 24;
        };
        long coordMask = switch (this.type) {
            case RuinedPortal -> 40;
            case Shipwreck -> 24;
            case TrialChamber -> 34;
            case Village -> 34;
            case OceanRuin -> 20;
            default -> 32;
        };
        long newFullSeed = ((fullSeed + regX * 341873128712L + regZ * 132897987541L + salt) ^ 25214903917L) & ((1L << 48) - 1);
        if (this.type == StructureTypes.Mansion) {
            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
            long x1 = (newFullSeed >> 17) % 60;
            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
            long x2 = (newFullSeed >> 17) % 60;
            long x = (x1 + x2) / 2;
            if (x == (((chunkX % 80) + 80) % 80)) {
                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
                long z1 = (newFullSeed >> 17) % 60;
                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
                long z2 = (newFullSeed >> 17) % 60;
                long z = (z1 + z2) / 2;
                return (z == (((chunkZ % 80) + 80) % 80));
            }
        }
        if (this.type == StructureTypes.OceanMonument) {
            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
            long x1 = (newFullSeed >> 17) % 27;
            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
            long x2 = (newFullSeed >> 17) % 27;
            long x = (x1 + x2) / 2;
            if (x == (((chunkX % 32) + 32) % 32)) {
                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
                long z1 = (newFullSeed >> 17) % 27;
                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
                long z2 = (newFullSeed >> 17) % 27;
                long z = (z1 + z2) / 2;
                return (z == (((chunkZ % 32) + 32) % 32));
            }
        }
        if (this.type == StructureTypes.AncientCity) {
            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
            if ((newFullSeed >> 44) == (((chunkX % 24) + 24) % 24)) {
                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
                if ((newFullSeed >> 44) == (((chunkZ % 24) + 24) % 24)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        newFullSeed = (newFullSeed * 25214903917L + 11) & ((1L << 48) - 1);
        if (((newFullSeed >> 17) % mask) == (((chunkX % coordMask) + coordMask) % coordMask)) {
            newFullSeed = (newFullSeed * 25214903917L + 11) & ((1L << 48) - 1);
            return ((((newFullSeed >> 17) % mask) + mask) % mask) == (((chunkZ % coordMask) + coordMask) % coordMask);
        }
        return false;
    }
}
