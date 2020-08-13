package nikita488.zycraft.api.multiblock;

public enum MultiValidationType
{
    CREATE,
    LOAD;

    public boolean shouldCreate()
    {
        return this == CREATE;
    }

    public boolean shouldLoad()
    {
        return this == LOAD;
    }
}
