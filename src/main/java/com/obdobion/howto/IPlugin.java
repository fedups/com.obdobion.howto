package com.obdobion.howto;

public interface IPlugin
{
    int execute(Context context);

    String getName();

    String getOverview();
}
