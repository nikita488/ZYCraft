package nikita488.zycraft;

import com.google.common.base.Suppliers;
import com.google.common.collect.Streams;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EmptyBlockReader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.dispenser.ZYBucketDispenseItemBehavior;
import nikita488.zycraft.init.*;
import nikita488.zycraft.init.worldgen.ZYConfiguredFeatures;
import nikita488.zycraft.init.worldgen.ZYFeatures;
import nikita488.zycraft.init.worldgen.ZYPlacements;
import nikita488.zycraft.multiblock.MultiChildType;
import nikita488.zycraft.multiblock.MultiPattern;
import nikita488.zycraft.multiblock.MultiType;
import nikita488.zycraft.network.ZYChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ZYCraft.MOD_ID)
public class ZYCraft
{
    public static final String MOD_ID = "zycraft";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<>(() -> Registrate.create(MOD_ID));

    public ZYCraft()
    {
        ZYBlocks.init();
        ZYItems.init();
        ZYTiles.init();
        ZYEntities.init();
        ZYParticles.init();
        ZYFeatures.init();
        ZYPlacements.init();
        ZYContainers.init();

        ZYRegistries.init();
        ZYMultiTypes.init();

        ZYTags.init();
        ZYGroups.init();
        ZYLang.init();
        ZYConfig.register();
        ZYChannel.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
/*        MultiPattern pattern = new MultiPattern(5, 5, 5);
        //pattern.setFrameMatcher(0, 0, 0, 4, 4, 4, 0);
        //pattern.setFacesMatcher(0, 0, 0, 4, 4, 4, 1);
        pattern.setShellMatcher(0, 0, 0, 4, 4, 4, 1);
        pattern.print(System.out);*/
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ZYConfiguredFeatures.init();
            DispenserBlock.registerDispenseBehavior(ZYItems.QUARTZ_BUCKET.get(), ZYBucketDispenseItemBehavior.INSTANCE);
        });

        //for (Block block : ForgeRegistries.BLOCKS.getValues())
        //    LOGGER.error("Block {} Material {}", block.getRegistryName().toString(), MultiChildType.fromState(block.getDefaultState(), EmptyBlockReader.INSTANCE, BlockPos.ZERO));
    }

    public static Registrate registrate()
    {
        return REGISTRATE.get();
    }

    public static ResourceLocation modLoc(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
