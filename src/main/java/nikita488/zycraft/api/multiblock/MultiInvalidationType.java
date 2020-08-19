package nikita488.zycraft.api.multiblock;

public enum MultiInvalidationType
{
    DESTROY,
    UNLOAD;

    public boolean shouldDestroy()
    {
        return this == DESTROY;
    }

    public boolean shouldUnload()
    {
        return this == UNLOAD;
    }
}
