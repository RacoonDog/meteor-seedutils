//package flexcoral.seedutils;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.RecursiveTask;
//
//class StructureChecker {
//    private List<int[]> coordinates;
//    private long salt;
//
//    public StructureChecker(long salt) {
//        this.coordinates = new ArrayList<>();
//        this.salt = salt;
//    }
//
//    public void addCoordinates(int chunkX, int chunkZ) {
//        coordinates.add(new int[]{chunkX, chunkZ});
//    }
//    public boolean canGenerateBuriedTreasure(long fullSeed){
//        for(int[] coord : coordinates){
//            if(!buriedTreasureChecker(fullSeed,coord[0],coord[1])){
//                return false;
//            }
//        }
//        return true;
//    }
//    public boolean buriedTreasureChecker(long fullseed,long chunkX,long chunkZ){
//        long newFullSeed = ((fullseed + chunkX * 341873128712L + chunkZ * 132897987541L + salt) ^ 25214903917L) & ((1L << 48) - 1);
//        newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//        long next24 = newFullSeed>>24;
//        if(next24<1677216.16){
//            return true;
//        }
//        return false;
//    }
//    public boolean canGenerateAtAll(long halfSeed, boolean isShip,boolean isMansion,boolean isChamber,boolean isOceanruin) {
//        for (int[] coord : coordinates) {
//            if (!canGenerateInTheFirstPlace(halfSeed, coord[0], coord[1], isShip,isMansion,isChamber,isOceanruin)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public boolean canActuallyGenerate(long fullSeed, boolean isShip, boolean isRuinedPortal, boolean isAncient,boolean isMansion,boolean isChamber,boolean isVillage,boolean isOceanruin,boolean isMonument) {
//        for (int[] coord : coordinates) {
//            if (!canActuallyGenerate(fullSeed, coord[0], coord[1], isShip, isRuinedPortal, isAncient,isMansion,isChamber,isVillage,isOceanruin,isMonument)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean canGenerateInTheFirstPlace(long halfSeed, int chunkX, int chunkZ, boolean isShip, boolean isMansion, boolean isChamber,boolean isOceanruin) {
//        int regX = isShip ? Math.floorDiv(chunkX , 24) : isMansion ? Math.floorDiv(chunkX , 80) : isChamber ? Math.floorDiv(chunkX , 34) : isOceanruin ? Math.floorDiv(chunkX,20) : chunkX >> 5;
//        int regZ = isShip ? Math.floorDiv(chunkZ , 24) : isMansion ? Math.floorDiv(chunkZ , 80) : isChamber ? Math.floorDiv(chunkZ , 34 ): isOceanruin ? Math.floorDiv(chunkZ,20) : chunkZ >> 5;
//        long mask = isShip ? 3 : isMansion ? 3 : isChamber ? 1 : isOceanruin ? 1 : 7;
//
//        long newHalfSeed = ((halfSeed + regX * 341873128712L + regZ * 132897987541L + salt) ^ 25214903917L) & ((1L << 32) - 1);
//        newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
//        if (isMansion) {
//            long x1 = (newHalfSeed >> 17) & mask;
//            newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
//            long x2 = (newHalfSeed >> 17) & mask;
//            long x = (x1 + x2) / 2;
//            if (x == (chunkX & mask)) {
//                newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
//                long z1 = (newHalfSeed >> 17) & mask;
//                newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
//                long z2 = (newHalfSeed >> 17) & mask;
//                long z = (z1 + z2) / 2;
//                return z == (chunkZ & mask);
//            }
//        }
//        if (((newHalfSeed >> 17) & mask) == (chunkX & mask)) {
//            newHalfSeed = (newHalfSeed * 25214903917L + 11) & ((1L << 32) - 1);
//            return ((newHalfSeed >> 17) & mask) == (chunkZ & mask);
//        }
//        return false;
//    }
//
//    private boolean canActuallyGenerate(long fullSeed, int chunkX, int chunkZ, boolean isShip, boolean isRuinedPortal, boolean isAncient, boolean isMansion, boolean isChamber, boolean isVillage, boolean isOceanruin,boolean isMonument) {
//        int regX = (isRuinedPortal ? Math.floorDiv(chunkX , 40) : isShip ? Math.floorDiv(chunkX , 24) : isAncient ? Math.floorDiv(chunkX , 24) : isMansion ? Math.floorDiv(chunkX , 80) : isChamber ? Math.floorDiv(chunkX , 34) : isVillage ? Math.floorDiv(chunkX , 34) : isOceanruin ? Math.floorDiv(chunkX,20) : isMonument ? Math.floorDiv(chunkX,32) : chunkX >> 5);
//        int regZ = (isRuinedPortal ? Math.floorDiv(chunkZ, 40) : isShip ? Math.floorDiv(chunkZ , 24) : isAncient ? Math.floorDiv(chunkZ , 24) : isMansion ? Math.floorDiv(chunkZ , 80): isChamber ? Math.floorDiv(chunkZ , 34) : isVillage ? Math.floorDiv(chunkZ , 34) : isOceanruin ? Math.floorDiv(chunkZ,20) : isMonument ? Math.floorDiv(chunkZ,32) : chunkZ >> 5);
//        long mask = isRuinedPortal ? 25 : isShip ? 20 : isChamber ? 22 : isVillage ? 26 : isOceanruin ? 12 : isOceanruin ? 27 :  24;
//        long coordMask = isRuinedPortal ? 40 : isShip ? 24 : isChamber ? 34 : isVillage ? 34 : isOceanruin ? 20 : 32;
//        long newFullSeed = ((fullSeed + regX * 341873128712L + regZ * 132897987541L + salt) ^ 25214903917L) & ((1L << 48) - 1);
//        if (isMansion) {
//            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//            long x1 = (newFullSeed >> 17) % 60;
//            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//            long x2 = (newFullSeed >> 17) % 60;
//            long x = (x1 + x2) / 2;
//            if (x == (((chunkX % 80) + 80) % 80)) {
//                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//                long z1 = (newFullSeed >> 17) % 60;
//                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//                long z2 = (newFullSeed >> 17) % 60;
//                long z = (z1 + z2) / 2;
//                return (z == (((chunkZ % 80) + 80) % 80));
//            }
//        }
//        if (isMonument) {
//            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//            long x1 = (newFullSeed >> 17) % 27;
//            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//            long x2 = (newFullSeed >> 17) % 27;
//            long x = (x1 + x2) / 2;
//            if (x == (((chunkX % 32) + 32) % 32)) {
//                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//                long z1 = (newFullSeed >> 17) % 27;
//                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//                long z2 = (newFullSeed >> 17) % 27;
//                long z = (z1 + z2) / 2;
//                return (z == (((chunkZ % 32) + 32) % 32));
//            }
//        }
//        if (isAncient) {
//            newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//            if ((newFullSeed >> 44) == (((chunkX % 24) + 24) % 24)) {
//                newFullSeed = ((newFullSeed * 25214903917L + 11) & ((1L << 48) - 1));
//                if ((newFullSeed >> 44) == (((chunkZ % 24) + 24) % 24)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        }
//
//        newFullSeed = (newFullSeed * 25214903917L + 11) & ((1L << 48) - 1);
//        if (((newFullSeed >> 17) % mask) == (((chunkX % coordMask) + coordMask) % coordMask)) {
//            newFullSeed = (newFullSeed * 25214903917L + 11) & ((1L << 48) - 1);
//            return ((((newFullSeed >> 17) % mask) + mask) % mask) == (((chunkZ % coordMask) + coordMask) % coordMask);
//        }
//        return false;
//    }
//
//}
//
//public class Main {
//    public static boolean printToFile = false;
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter the filename: ");
//        String filename = scanner.nextLine();
//        float startTime = System.nanoTime();
//        File file = new File(filename);
//        InputStream inputStream = null;
//
//        try {
//            inputStream = file.exists() ? new FileInputStream(file) : Main.class.getResourceAsStream("/" + filename);
//            if (inputStream == null) {
//                throw new IOException("File not found: " + filename);
//            }
//        } catch (IOException e) {
//            System.out.println("Error opening file: " + e.getMessage());
//            return;
//        }
//
//        List<Long> structureSeeds = new ArrayList<>();
//
//        try (Scanner fileScanner = new Scanner(inputStream)) {
//            long pillarSeed = fileScanner.nextLong();
//            long gateWay = fileScanner.nextLong();
//            fileScanner.nextLine();
//
//            StructureChecker desertTempleChecker = new StructureChecker(14357617L);
//            StructureChecker jungleTempleChecker = new StructureChecker(14357619L);
//            StructureChecker iglooChecker = new StructureChecker(14357618L);
//            StructureChecker swampHutChecker = new StructureChecker(14357620L);
//            StructureChecker outpostChecker = new StructureChecker(165745296);
//            StructureChecker shipwreckChecker = new StructureChecker(165745295);
//            StructureChecker ruinedPortalChecker = new StructureChecker(34222645);
//            StructureChecker ancientCityChecker = new StructureChecker(20083232);
//            StructureChecker mansionChecker = new StructureChecker(10387319);
//            StructureChecker chamberChecker = new StructureChecker(94251327);
//            StructureChecker villageChecker = new StructureChecker(10387312);
//            StructureChecker oceanruinChecker = new StructureChecker(14357621);
//            StructureChecker buriedChecker = new StructureChecker(10387320);
//            StructureChecker monumentChecker = new StructureChecker(10387313);
//            StructureChecker trailruinChecker = new StructureChecker(83469867);
//            StructureChecker structurefeatures1_12Checker = new StructureChecker(14357617);
//            StructureChecker village1_17Checker = new StructureChecker(10387312);
//            while (fileScanner.hasNextLine()) {
//                String line = fileScanner.nextLine().trim();
//                if (line.equalsIgnoreCase("true") || line.equalsIgnoreCase("false")) {
//                    boolean findStructureSeed = Boolean.parseBoolean(line);
//                    line = fileScanner.nextLine().trim();
//                    Main.printToFile = Boolean.parseBoolean(line);
//                    if (pillarSeed == -1) {
//                        crackSeedsWithoutPillars(desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker,ancientCityChecker,mansionChecker,chamberChecker, villageChecker, oceanruinChecker,buriedChecker,monumentChecker,trailruinChecker, structurefeatures1_12Checker,village1_17Checker,gateWay,findStructureSeed, structureSeeds);
//                    } else {
//                        crackSeeds(pillarSeed, gateWay, desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker, ancientCityChecker,mansionChecker,chamberChecker,villageChecker, oceanruinChecker,buriedChecker, monumentChecker,trailruinChecker, structurefeatures1_12Checker,village1_17Checker,findStructureSeed, structureSeeds);
//                    }
//                    break;
//                } else {
//                    String[] parts = line.split("\\s+");
//                    if (parts.length == 3) {
//                        String structureType = parts[0].toLowerCase();
//                        int chunkX = Integer.parseInt(parts[1]);
//                        int chunkZ = Integer.parseInt(parts[2]);
//
//                        switch (structureType) {
//                            case "desert":
//                                desertTempleChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "jungle":
//                                jungleTempleChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "igloo":
//                                iglooChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "swamp":
//                                swampHutChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "outpost":
//                                outpostChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "ship":
//                                shipwreckChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "portal":
//                                ruinedPortalChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "ancient":
//                                ancientCityChecker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "mansion":
//                                mansionChecker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "chamber":
//                                chamberChecker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "village":
//                                villageChecker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "ocean":
//                                oceanruinChecker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "treasure":
//                                buriedChecker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "monument":
//                                monumentChecker.addCoordinates(chunkX, chunkZ);
//                                break;
//                            case "trailruins":
//                                trailruinChecker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "oldstructure":
//                                structurefeatures1_12Checker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            case "oldvillage":
//                                village1_17Checker.addCoordinates(chunkX,chunkZ);
//                                break;
//                            default:
//                                System.out.println("Unknown structure type: " + structureType);
//                        }
//                    } else {
//                        System.out.println("Invalid line format: " + line);
//                    }
//                }
//            }
//        }
//
//        try (FileWriter writer = new FileWriter("structureseeds.txt")) {
//            for (Long seed : structureSeeds) {
//                if (printToFile) {
//                    writer.write(seed + "\n");
//                }
//                System.out.println("Found valid structure seed: " + seed);
//            }
//        } catch (IOException e) {
//            System.out.println("Error writing to file: " + e.getMessage());
//        }
//        Scanner closing = new Scanner(System.in);
//        System.out.println("Programm finished and it took "+((System.nanoTime()-startTime)/1000000000)+" seconds");
//        System.out.println("Press enter to close");
//        String close = scanner.nextLine();
//    }
//
//    private static void crackSeeds(long pillarSeed, long gateWay, StructureChecker desertTempleChecker, StructureChecker jungleTempleChecker, StructureChecker iglooChecker, StructureChecker swampHutChecker, StructureChecker outpostChecker, StructureChecker shipwreckChecker, StructureChecker ruinedPortalChecker,StructureChecker ancientCityChecker,StructureChecker mansionChecker,StructureChecker chamberChecker,StructureChecker villageChecker,StructureChecker oceanruinChecker,StructureChecker buriedChecker, StructureChecker monumentChecker,StructureChecker trailruinChecker,StructureChecker structurefeatures1_12Checker,StructureChecker village1_17Checker,boolean findStructureSeed, List<Long> structureSeeds) {
//        ForkJoinPool pool = new ForkJoinPool();
//        pool.invoke(new SeedCrackerTask(pillarSeed, gateWay, desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker, ancientCityChecker, mansionChecker , chamberChecker,villageChecker,oceanruinChecker,buriedChecker,monumentChecker,trailruinChecker,structurefeatures1_12Checker,village1_17Checker,findStructureSeed, structureSeeds, 0, 65536));
//    }
//
//    private static void crackSeedsWithoutPillars(StructureChecker desertTempleChecker, StructureChecker jungleTempleChecker, StructureChecker iglooChecker, StructureChecker swampHutChecker, StructureChecker outpostChecker, StructureChecker shipwreckChecker, StructureChecker ruinedPortalChecker,StructureChecker ancientCityChecker,StructureChecker mansionChecker,StructureChecker chamberChecker,StructureChecker villageChecker,StructureChecker oceanruinChecker, StructureChecker buriedChecker ,StructureChecker monumentChecker,StructureChecker trailruinChecker,StructureChecker structurefeatures1_12Checker,StructureChecker village1_17Checker,long gateWay, boolean findStructureSeed, List<Long> structureSeeds) {
//        ForkJoinPool pool = new ForkJoinPool();
//        pool.invoke(new SeedCrackerTaskNoPillars(desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker,ancientCityChecker,mansionChecker,chamberChecker, villageChecker,oceanruinChecker,buriedChecker,monumentChecker,trailruinChecker,structurefeatures1_12Checker,village1_17Checker,gateWay, findStructureSeed, structureSeeds, 0, 1048575));
//    }
//
//    private static class SeedCrackerTask extends RecursiveTask<Void> {
//        private static final int THRESHOLD = 1024;
//        private long pillarSeed;
//        private long gateWay;
//        private StructureChecker desertTempleChecker;
//        private StructureChecker jungleTempleChecker;
//        private StructureChecker iglooChecker;
//        private StructureChecker swampHutChecker;
//        private StructureChecker outpostChecker;
//        private StructureChecker shipwreckChecker;
//        private StructureChecker ruinedPortalChecker;
//        private StructureChecker ancientCityChecker;
//
//        private StructureChecker mansionChecker;
//
//        private StructureChecker chamberChecker;
//
//        private StructureChecker villageChecker;
//
//        private StructureChecker oceanruinChecker;
//
//        private StructureChecker buriedChecker;
//
//        private StructureChecker monumentChecker;
//
//        private StructureChecker trailruinChecker;
//
//        private StructureChecker structurefeatures1_12checker;
//
//        private StructureChecker village1_17Checker;
//        private boolean findStructureSeed;
//        private List<Long> structureSeeds;
//        private long start;
//        private long end;
//
//        public SeedCrackerTask(long pillarSeed, long gateWay, StructureChecker desertTempleChecker, StructureChecker jungleTempleChecker, StructureChecker iglooChecker, StructureChecker swampHutChecker, StructureChecker outpostChecker, StructureChecker shipwreckChecker, StructureChecker ruinedPortalChecker, StructureChecker ancientCityChecker,StructureChecker mansionChecker,StructureChecker chamberChecker,StructureChecker villageChecker,StructureChecker oceanruinChecker,StructureChecker buriedChecker,StructureChecker monumentChecker,StructureChecker trailruinChecker,StructureChecker structurefeatures1_12checker,StructureChecker village1_17Checker,boolean findStructureSeed, List<Long> structureSeeds, long start, long end) {
//            this.pillarSeed = pillarSeed;
//            this.gateWay = gateWay;
//            this.desertTempleChecker = desertTempleChecker;
//            this.jungleTempleChecker = jungleTempleChecker;
//            this.iglooChecker = iglooChecker;
//            this.swampHutChecker = swampHutChecker;
//            this.outpostChecker = outpostChecker;
//            this.shipwreckChecker = shipwreckChecker;
//            this.ruinedPortalChecker = ruinedPortalChecker;
//            this.ancientCityChecker = ancientCityChecker;
//            this.mansionChecker = mansionChecker;
//            this.chamberChecker = chamberChecker;
//            this.villageChecker = villageChecker;
//            this.oceanruinChecker = oceanruinChecker;
//            this.buriedChecker = buriedChecker;
//            this.monumentChecker = monumentChecker;
//            this.trailruinChecker = trailruinChecker;
//            this.structurefeatures1_12checker = structurefeatures1_12checker;
//            this.village1_17Checker = village1_17Checker;
//            this.findStructureSeed = findStructureSeed;
//            this.structureSeeds = structureSeeds;
//            this.start = start;
//            this.end = end;
//        }
//
//        @Override
//        protected Void compute() {
//            if (end - start <= THRESHOLD) {
//                for (long lowerBits = start; lowerBits < end; lowerBits++) {
//                    long partialRandomInternalState = (pillarSeed << 16) + lowerBits;
//                    long halfSeed = ((1540035429L * (1540035429L * partialRandomInternalState + 239479465L) + 239479465L) ^ 25214903917L) & 4294967295L;
//
//                    if (canGenerateAtAll(halfSeed)) {
//                        if (gateWay != -1) {
//                            long nextSeed = halfSeed ^ 25214903917L;
//                            nextSeed = (nextSeed * 25214903917L + 11) & ((1L << 32) - 1);
//                            if (((nextSeed >> 17) & 3) == (gateWay & 3)) {
//                                processHalfSeed(halfSeed);
//                            }
//                        } else {
//                            processHalfSeed(halfSeed);
//                        }
//                    }
//                }
//                return null;
//            } else {
//                long mid = (start + end) >>> 1;
//                SeedCrackerTask left = new SeedCrackerTask(pillarSeed, gateWay, desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker, ancientCityChecker,mansionChecker,chamberChecker,villageChecker,oceanruinChecker,buriedChecker,monumentChecker,trailruinChecker,structurefeatures1_12checker,village1_17Checker,findStructureSeed, structureSeeds, start, mid);
//                SeedCrackerTask right = new SeedCrackerTask(pillarSeed, gateWay, desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker, ancientCityChecker,mansionChecker,chamberChecker,villageChecker,oceanruinChecker,buriedChecker,monumentChecker,trailruinChecker,structurefeatures1_12checker,village1_17Checker,findStructureSeed, structureSeeds, mid, end);
//                invokeAll(left, right);
//                return null;
//            }
//        }
//
//        private void processHalfSeed(long halfSeed) {
//            System.out.println("Found halfSeed: " + halfSeed);
//            if (findStructureSeed) {
//                crackStructureSeed(halfSeed);
//            }
//        }
//
//        private boolean canGenerateAtAll(long halfSeed) {
//            return desertTempleChecker.canGenerateAtAll(halfSeed, false,false,false,false) &&
//                jungleTempleChecker.canGenerateAtAll(halfSeed, false,false,false,false) &&
//                iglooChecker.canGenerateAtAll(halfSeed, false,false,false,false) &&
//                swampHutChecker.canGenerateAtAll(halfSeed, false,false,false,false) &&
//                outpostChecker.canGenerateAtAll(halfSeed, false,false,false,false) &&
//                shipwreckChecker.canGenerateAtAll(halfSeed, true,false,false,false)&&
//                mansionChecker.canGenerateAtAll(halfSeed,false, true,false,false)&&
//                chamberChecker.canGenerateAtAll(halfSeed,false,false,true,false)&&
//                villageChecker.canGenerateAtAll(halfSeed,false,false,true,false)&&//method to bitlift chambers can be applied to villages without any changes
//                oceanruinChecker.canGenerateAtAll(halfSeed,false,false,false,true)&&
//                trailruinChecker.canGenerateAtAll(halfSeed,false,false,true,false)&&//same as villages, this works with trial chambers
//                structurefeatures1_12checker.canGenerateAtAll(halfSeed,false,false,false,false)&&
//                village1_17Checker.canGenerateAtAll(halfSeed,false,false,false,false);
//        }
//
//        private void crackStructureSeed(long halfSeed) {
//            for (long structureSeed = halfSeed; structureSeed < (1L << 48); structureSeed += (1L << 32)) {
//                if (canActuallyGenerate(structureSeed)) {
//                    long nextSeed = structureSeed ^ 25214903917L;
//                    nextSeed = (nextSeed * 25214903917L + 11) & ((1L << 48) - 1);
//                    if (((nextSeed >> 17) % 20) == gateWay || gateWay == -1) {
//                        synchronized (structureSeeds) {
//                            structureSeeds.add(structureSeed);
//                        }
//                    }
//                }
//            }
//        }
//
//        private boolean canActuallyGenerate(long structureSeed) {
//            return desertTempleChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                jungleTempleChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                iglooChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                swampHutChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                outpostChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                shipwreckChecker.canActuallyGenerate(structureSeed, true, false,false,false,false,false,false,false) &&
//                ruinedPortalChecker.canActuallyGenerate(structureSeed, false, true,false,false,false,false,false,false)&&
//                ancientCityChecker.canActuallyGenerate(structureSeed, false, false,true,false,false,false,false,false)&&
//                mansionChecker.canActuallyGenerate(structureSeed,false,false,false,true,false,false,false,false)&&
//                chamberChecker.canActuallyGenerate(structureSeed,false,false,false,false,true,false,false,false)&&
//                villageChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,true,false,false)&&
//                oceanruinChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,true,false)&&
//                buriedChecker.canGenerateBuriedTreasure(structureSeed)&&
//                monumentChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,false,true)&&
//                trailruinChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,true,false,false)&&
//                structurefeatures1_12checker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,false,false)&&
//                village1_17Checker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,false,false);
//        }
//    }
//
//    private static class SeedCrackerTaskNoPillars extends RecursiveTask<Void> {
//        private static final long THRESHOLD = 1024L;
//        private StructureChecker desertTempleChecker;
//        private StructureChecker jungleTempleChecker;
//        private StructureChecker iglooChecker;
//        private StructureChecker swampHutChecker;
//        private StructureChecker outpostChecker;
//        private StructureChecker shipwreckChecker;
//        private StructureChecker ruinedPortalChecker;
//
//        private StructureChecker ancientCityChecker;
//
//        private StructureChecker mansionChecker;
//        private StructureChecker chamberChecker;
//
//        private StructureChecker villageChecker;
//
//        private StructureChecker oceanruinChecker;
//
//        private StructureChecker buriedChecker;
//
//        private StructureChecker monumentChecker;
//
//        private StructureChecker trailruinChecker;
//
//        private StructureChecker structurefeatures1_12Checker;
//
//        private StructureChecker village1_17Checker;
//        private long gateWay;
//        private boolean findStructureSeed;
//        private List<Long> structureSeeds;
//        private long start;
//        private long end;
//
//        public SeedCrackerTaskNoPillars(StructureChecker desertTempleChecker, StructureChecker jungleTempleChecker, StructureChecker iglooChecker, StructureChecker swampHutChecker, StructureChecker outpostChecker, StructureChecker shipwreckChecker, StructureChecker ruinedPortalChecker,StructureChecker ancientCityChecker,StructureChecker mansionChecker,StructureChecker chamberChecker,StructureChecker villageChecker,StructureChecker oceanruinChecker,StructureChecker buriedChecker,StructureChecker monumentChecker,StructureChecker trailruinChecker, StructureChecker structurefeatures1_12Checker,StructureChecker village1_17Checker,long gateWay, boolean findStructureSeed, List<Long> structureSeeds, long start, long end) {
//            this.desertTempleChecker = desertTempleChecker;
//            this.jungleTempleChecker = jungleTempleChecker;
//            this.iglooChecker = iglooChecker;
//            this.swampHutChecker = swampHutChecker;
//            this.outpostChecker = outpostChecker;
//            this.shipwreckChecker = shipwreckChecker;
//            this.ruinedPortalChecker = ruinedPortalChecker;
//            this.ancientCityChecker = ancientCityChecker;
//            this.mansionChecker = mansionChecker;
//            this.chamberChecker = chamberChecker;
//            this.villageChecker = villageChecker;
//            this.oceanruinChecker = oceanruinChecker;
//            this.buriedChecker = buriedChecker;
//            this.monumentChecker = monumentChecker;
//            this.trailruinChecker = trailruinChecker;
//            this.structurefeatures1_12Checker = structurefeatures1_12Checker;
//            this.village1_17Checker = village1_17Checker;
//            this.gateWay = gateWay;
//            this.findStructureSeed = findStructureSeed;
//            this.structureSeeds = structureSeeds;
//            this.start = start;
//            this.end = end;
//        }
//
//        @Override
//        protected Void compute() {
//            if (end - start <= THRESHOLD) {
//                for (long lower20bits = start; lower20bits < end; lower20bits++) {
//                    if (canGenerateAtAll(lower20bits)) {
//                        processHalfSeed(lower20bits);
//                    }
//                }
//                return null;
//            } else {
//                long mid = (start + end) >>> 1;
//                SeedCrackerTaskNoPillars left = new SeedCrackerTaskNoPillars(desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker, ancientCityChecker,mansionChecker,chamberChecker,villageChecker,oceanruinChecker,buriedChecker,monumentChecker,trailruinChecker,structurefeatures1_12Checker,village1_17Checker,gateWay, findStructureSeed, structureSeeds, start, mid);
//                SeedCrackerTaskNoPillars right = new SeedCrackerTaskNoPillars(desertTempleChecker, jungleTempleChecker, iglooChecker, swampHutChecker, outpostChecker, shipwreckChecker, ruinedPortalChecker, ancientCityChecker,mansionChecker, chamberChecker,villageChecker,oceanruinChecker,buriedChecker,monumentChecker,trailruinChecker,structurefeatures1_12Checker,village1_17Checker,gateWay, findStructureSeed, structureSeeds, mid, end);
//                invokeAll(left, right);
//                return null;
//            }
//        }
//
//        private void processHalfSeed(long lower20bits) {
//            System.out.println("Found 20 bit seed: " + lower20bits);
//            if (findStructureSeed) {
//                crackStructureSeed(lower20bits);
//            }
//        }
//
//        private boolean canGenerateAtAll(long lower20bits) {
//            return desertTempleChecker.canGenerateAtAll(lower20bits, false,false,false,false) &&
//                jungleTempleChecker.canGenerateAtAll(lower20bits, false,false,false,false) &&
//                iglooChecker.canGenerateAtAll(lower20bits, false,false,false,false) &&
//                swampHutChecker.canGenerateAtAll(lower20bits, false,false,false,false) &&
//                outpostChecker.canGenerateAtAll(lower20bits, false,false,false,false) &&
//                shipwreckChecker.canGenerateAtAll(lower20bits, true,false,false,false)&&
//                mansionChecker.canGenerateAtAll(lower20bits,false,true,false,false)&&
//                chamberChecker.canGenerateAtAll(lower20bits,false,false,true,false)&&
//                villageChecker.canGenerateAtAll(lower20bits,false,false,true,false)&&//using the same algorithm from chambers for villages because its just easier and cleaner
//                oceanruinChecker.canGenerateAtAll(lower20bits,false,false,false,true)&&
//                trailruinChecker.canGenerateAtAll(lower20bits,false,false,true,false)&&//same as village
//                structurefeatures1_12Checker.canGenerateAtAll(lower20bits,false,false,false,false)&&
//                village1_17Checker.canGenerateAtAll(lower20bits,false,false,false,false);
//        }
//
//        private void crackStructureSeed(long lower20bits) {
//            for (long structureSeed = lower20bits; structureSeed < (1L << 48); structureSeed += (1L << 20)) {
//                if (canActuallyGenerate(structureSeed)) {
//                    long nextSeed = structureSeed ^ 25214903917L;
//                    nextSeed = (nextSeed * 25214903917L + 11) & ((1L << 48) - 1);
//                    if (((nextSeed >> 17) % 20) == gateWay || gateWay == -1) {
//                        synchronized (structureSeeds) {
//                            structureSeeds.add(structureSeed);
//                        }
//                    }
//                }
//            }
//        }
//
//        private boolean canActuallyGenerate(long structureSeed) {
//            return desertTempleChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                jungleTempleChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                iglooChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                swampHutChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                outpostChecker.canActuallyGenerate(structureSeed, false, false,false,false,false,false,false,false) &&
//                shipwreckChecker.canActuallyGenerate(structureSeed, true, false,false,false,false,false,false,false) &&
//                ruinedPortalChecker.canActuallyGenerate(structureSeed, false, true,false,false,false,false,false,false)&&
//                ancientCityChecker.canActuallyGenerate(structureSeed, false, false,true,false,false,false,false,false)&&
//                mansionChecker.canActuallyGenerate(structureSeed,false,false,false,true,false,false,false,false)&&
//                chamberChecker.canActuallyGenerate(structureSeed,false,false,false,false,true,false,false,false)&&
//                villageChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,true,false,false)&&
//                oceanruinChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,true,false)&&
//                buriedChecker.canGenerateBuriedTreasure(structureSeed)&&
//                monumentChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,false,true)&&
//                trailruinChecker.canActuallyGenerate(structureSeed,false,false,false,false,false,true,false,false)&&
//                structurefeatures1_12Checker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,false,false)&&
//                village1_17Checker.canActuallyGenerate(structureSeed,false,false,false,false,false,false,false,false);
//        }
//    }
//}
