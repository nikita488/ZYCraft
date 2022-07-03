package nikita488.zycraft.compat.jade;

import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.block.FluidSelectorBlock;
import nikita488.zycraft.block.FluidVoidBlock;
import nikita488.zycraft.block.PaintableViewerBlock;
import nikita488.zycraft.block.ZychoriumWaterBlock;
import nikita488.zycraft.block.entity.ColorableBlockEntity;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.block.entity.ValveBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ZYHwylaPluign implements IWailaPlugin
{
    @Override
    public void register(IWailaCommonRegistration registration)
    {
        registration.registerBlockDataProvider(ColorableComponentProvider.INSTANCE, ColorableBlockEntity.class);
        registration.registerBlockDataProvider(ValveComponentProvider.INSTANCE, ValveBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration)
    {
        registration.addConfig(ColorableComponentProvider.KEY, true);
        registration.addConfig(FluidSourceComponentProvider.KEY, true);
        registration.addConfig(FluidVoidComponentProvider.KEY, true);
        registration.addConfig(FabricatorComponentProvider.KEY, true);
        registration.addConfig(ValveComponentProvider.KEY, true);
        registration.addConfig(ItemIOComponentProvider.KEY, true);

        registration.registerBlockComponent(ColorableComponentProvider.INSTANCE, ColorableBlock.class);
        registration.registerBlockComponent(ColorableComponentProvider.INSTANCE, PaintableViewerBlock.class);
        registration.registerBlockComponent(FluidSourceComponentProvider.INSTANCE, ZychoriumWaterBlock.class);
        registration.registerBlockComponent(FluidSourceComponentProvider.INSTANCE, FluidSelectorBlock.class);
        registration.registerBlockComponent(FluidVoidComponentProvider.INSTANCE, FluidVoidBlock.class);
        registration.registerBlockComponent(FabricatorComponentProvider.INSTANCE, FabricatorBlock.class);
        registration.registerBlockComponent(ValveComponentProvider.INSTANCE, ValveBlock.class);
        registration.registerBlockComponent(ItemIOComponentProvider.INSTANCE, ItemIOBlock.class);
    }

    public static void init()
    {
        addConfigLang(ColorableComponentProvider.KEY, "Colorable");
        addConfigLang(FluidSourceComponentProvider.KEY, "Fluid Source");
        addConfigLang(FluidVoidComponentProvider.KEY, "Fluid Void");
        addConfigLang(FabricatorComponentProvider.KEY, "Fabricator");
        addConfigLang(ValveComponentProvider.KEY, "Valve");
        addConfigLang(ItemIOComponentProvider.KEY, "Item IO");
    }

    private static void addConfigLang(ResourceLocation key, String localizedName)
    {
        ZYCraft.registrate().addRawLang("config.jade.plugin_" + key.getNamespace() + "." + key.getPath(), localizedName);
    }
}
