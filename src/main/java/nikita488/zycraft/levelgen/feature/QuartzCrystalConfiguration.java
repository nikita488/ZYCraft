package nikita488.zycraft.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class QuartzCrystalConfiguration implements FeatureConfiguration
{
    public static final Codec<QuartzCrystalConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(RuleTest.CODEC.fieldOf("can_be_placed_on").forGetter(config -> config.canBePlacedOn),
                    Codec.intRange(1, 5).fieldOf("max_crystals").orElse(5).forGetter(config -> config.maxCrystals))
            .apply(instance, QuartzCrystalConfiguration::new));

    public final RuleTest canBePlacedOn;
    public final int maxCrystals;

    public QuartzCrystalConfiguration(RuleTest canBePlacedOn, int maxCrystals)
    {
        this.canBePlacedOn = canBePlacedOn;
        this.maxCrystals = maxCrystals;
    }
}
