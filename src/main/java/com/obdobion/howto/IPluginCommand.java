package com.obdobion.howto;

/**
 * <p>
 * IPluginCommand interface.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public interface IPluginCommand
{
    /**
     * <p>
     * execute.
     * </p>
     *
     * @param context
     *            a {@link com.obdobion.howto.Context} object.
     * @return a int.
     */
    int execute(Context context);

    /**
     * <p>
     * getGroup.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getGroup();

    /**
     * <p>
     * getName.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getName();

    /**
     * <p>
     * getOverview.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getOverview();

    /**
     * <p>isOnceAndDone.</p>
     *
     * @return a boolean.
     */
    boolean isOnceAndDone();
}
