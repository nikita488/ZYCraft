package nikita488.zycraft.world.gen.feature.сonfig;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ZychoriteVeinFeatureConfig implements IFeatureConfig
{
    public final OreFeatureConfig.FillerBlockType replaceableBlock;
    public final int size;
    public final float orePercentage;

    public ZychoriteVeinFeatureConfig(OreFeatureConfig.FillerBlockType replaceableBlock, int size, float orePercentage)
    {
        this.replaceableBlock = replaceableBlock;
        this.size = size;
        this.orePercentage = orePercentage;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops)
    {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("replaceableBlock"), ops.createString(replaceableBlock.getName()),
                ops.createString("size"), ops.createInt(size),
                ops.createString("orePercentage"), ops.createFloat(orePercentage))));
    }

    public static ZychoriteVeinFeatureConfig deserialize(Dynamic<?> dynamic)
    {
        OreFeatureConfig.FillerBlockType target = OreFeatureConfig.FillerBlockType.byName(dynamic.get("replaceableBlock").asString(""));
        int size = dynamic.get("size").asInt(0);
        float orePercentage = dynamic.get("orePercentage").asFloat(0.0F);
        return new ZychoriteVeinFeatureConfig(target, size, orePercentage);
    }
}
