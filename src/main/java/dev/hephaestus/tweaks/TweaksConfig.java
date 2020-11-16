package dev.hephaestus.tweaks;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "tinytweaks")
@Config.Gui.Background("minecraft:textures/block/hay_block_side.png")
public class TweaksConfig implements ConfigData {
    // - Plants! --------------------------------------------------------------
    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.Tooltip
    public boolean easyHarvestCrops = true;

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean easyHarvestSugarcane = true;

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean easyHarvestDropAsItems = false;

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.CollapsibleObject
    public AutoPlanting autoPlanting = new AutoPlanting();

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.CollapsibleObject
    public Rejuvenation rejuvenation = new Rejuvenation();

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.Tooltip(count = 4)
    public boolean plantHitboxes = false;

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.CollapsibleObject
    public LeavesConfig leaves = new LeavesConfig();

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.Tooltip
    public boolean instantLeafDecay = false;

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean betterLilyPads = true;

    @ConfigEntry.Category("plants")
    @ConfigEntry.Gui.Tooltip
    public boolean farmerVillagerAutomation = true;

    // - Animals! -------------------------------------------------------------
    @ConfigEntry.Category("animals")
    @ConfigEntry.Gui.Tooltip
    public boolean animalsEatOffGround = true;

    @ConfigEntry.Category("animals")
    @ConfigEntry.Gui.Tooltip
    public boolean breedWildWolves = true;

    // - Nether! --------------------------------------------------------------
    @ConfigEntry.Category("nether")
    @ConfigEntry.Gui.CollapsibleObject
    public NetherRejuvenation netherRejuvenation = new NetherRejuvenation();

    @ConfigEntry.Category("nether")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean blueSoulFireEffects = true;

    @ConfigEntry.Category("nether")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean soulFireDoesMoreDamage = false;

    // - Miscellaneous! -------------------------------------------------------
    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.CollapsibleObject
    public FlintAndSteelConfig flintAndSteel = new FlintAndSteelConfig();

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean lanternBlastResistance = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.CollapsibleObject
    public NamesAndThings namesAndThings = new NamesAndThings();

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip(count = 4)
    public boolean mossyThings = true;

    @ConfigEntry.Category("misc")
    public boolean burningLogsDropCharcoal = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean doubleDoors = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean bubbleColumnsFlow = false;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip
    public boolean infiniteCauldrons = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean easyXp = true;

    public static class AutoPlanting {
        @ConfigEntry.Gui.Tooltip(count = 4)
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        public int delay = 20;
    }

    public static class Rejuvenation {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        public double grassGrowthRate = 0.0125D;

        @ConfigEntry.Gui.Tooltip(count = 5)
        public double thePowerOfFriendship = 1.25;

        @ConfigEntry.Gui.Tooltip(count = 2)
        public float longGrass = 0.01F;

        @ConfigEntry.Gui.Tooltip
        public boolean saplings = true;
    }

    public static class NetherRejuvenation {
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        public double rootsGrowthRate = 0.0125D;

        @ConfigEntry.Gui.Tooltip(count = 5)
        public double thePowerOfFriendship = 1.25;

        @ConfigEntry.Gui.Tooltip(count = 2)
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int sproutRootsRatio = 75;

        @ConfigEntry.Gui.Tooltip(count = 2)
        public double vinesChance = 0.01;
    }

    public static class LeavesConfig {
        public boolean collide = false;
        @ConfigEntry.Gui.Tooltip
        public boolean persistentCollide = false;

        @ConfigEntry.Gui.Tooltip
        public boolean slow = true;

        public double slowAmount = 0.75D;

        public boolean climb = true;

        @ConfigEntry.Gui.Tooltip(count = 2)
        public double treeClimbingSpeed = 0.75D;
    }

    public static class FlintAndSteelConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;
        public int burnTime = 3;
    }

    public static class NamesAndThings {
        @ConfigEntry.Gui.Tooltip
        public boolean containerLabels = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 200)
        public long labelScale = 100;
    }
}