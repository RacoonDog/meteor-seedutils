package flexcoral.seedutils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SeedCrackerTask extends RecursiveTask<Void> {
    private final long THRESHOLD = 1024L;
    private List<StructureChecker> checkers = new ArrayList<>();
    private Integer pillarSeed;
    private Integer gatewayIndex;
    private long start;
    private long end;
    private List<Long> structureSeeds;

    public SeedCrackerTask(List<StructureChecker> checkers, Integer pillarSeed, Integer gatewayIndex, List<Long> structureSeeds, long start, long end) {
        this.checkers = checkers;
        this.pillarSeed = pillarSeed;
        this.gatewayIndex = gatewayIndex;
        this.start = start;
        this.end = end;
        this.structureSeeds = structureSeeds;
    }

    @Override
    protected Void compute() {
        if (end - start <= THRESHOLD) {
            for (long lower20bits = start; lower20bits < end; lower20bits++) {
                if (canGenerateAtAll(lower20bits)) {
                    System.out.println("Found 20 bit seed: " + lower20bits);
                    crackStructureSeed(lower20bits);
                }
            }
        } else {
            long mid = (start + end) >>> 1;
            SeedCrackerTask left = new SeedCrackerTask(checkers, pillarSeed, gatewayIndex, structureSeeds, start, mid);
            SeedCrackerTask right = new SeedCrackerTask(checkers, pillarSeed, gatewayIndex, structureSeeds, mid, end);
            invokeAll(left, right);
        }
        return null;
    }

    private boolean canGenerateAtAll(long lower20bits) {
        for (StructureChecker checker : checkers) {
            if (!checker.canGenerateAtAll(lower20bits)) {
                return false;
            }
        }
        return true;
    }

    private void crackStructureSeed(long lower20bits) {
        for (long structureSeed = lower20bits; structureSeed < (1L << 48); structureSeed += (1L << 20)) {
            if (canActuallyGenerate(structureSeed)) {
                long nextSeed = structureSeed ^ 25214903917L;
                nextSeed = (nextSeed * 25214903917L + 11) & ((1L << 48) - 1);
                if (gatewayIndex == null || ((nextSeed >> 17) % 20) == gatewayIndex) {
                    synchronized (structureSeeds) {
                        System.out.println("Found structure seed: " + structureSeed);
                        structureSeeds.add(structureSeed);
                    }
                }
            }
        }
    }

    private boolean canActuallyGenerate(long lower20bits) {
        for (StructureChecker checker : checkers) {
            if (!checker.canActuallyGenerate(lower20bits)) {
                return false;
            }
        }
        return true;
    }
}
