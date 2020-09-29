package nikita488.zycraft.util;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.LogicalSide;
import nikita488.zycraft.init.ZYTags;

import java.nio.file.Path;
import java.util.function.Function;

public class ZYItemTagsProvider extends TagsProvider<Item> implements RegistrateProvider
{
    private final AbstractRegistrate<?> owner;
    private final Function<ITag.INamedTag<Block>, ITag.Builder> blockTagResolver;

    public ZYItemTagsProvider(AbstractRegistrate<?> owner, RegistrateTagsProvider<Block> blockTagProvider, DataGenerator generator, ExistingFileHelper helper)
    {
        super(generator, Registry.ITEM, owner.getModid(), helper);
        this.owner = owner;
        this.blockTagResolver = blockTagProvider::createBuilderIfAbsent;
    }

    public void copy(ITag.INamedTag<Block> blockTag, ITag.INamedTag<Item> itemTag)
    {
        ITag.Builder itemTagBuilder = this.createBuilderIfAbsent(itemTag);
        ITag.Builder blockTagBuilder = this.blockTagResolver.apply(blockTag);
        blockTagBuilder.getProxyStream().forEach(itemTagBuilder::addProxyTag);
    }

    @Override
    protected Path makePath(ResourceLocation id)
    {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
    }

    @Override
    public String getName()
    {
        return "Tags (items)";
    }

    @Override
    protected void registerTags()
    {
        owner.genData(ZYTags.ITEM_TAGS, this);
    }

    @Override
    public LogicalSide getSide()
    {
        return LogicalSide.SERVER;
    }

    @Override
    public Builder<Item> getOrCreateBuilder(ITag.INamedTag<Item> tag)
    {
        return super.getOrCreateBuilder(tag);
    }

    @Override
    public ITag.Builder createBuilderIfAbsent(ITag.INamedTag<Item> tag)
    {
        return super.createBuilderIfAbsent(tag);
    }
}
