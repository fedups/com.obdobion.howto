package com.obdobion.howto.module.calc;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.obdobion.algebrain.Equ;
import com.obdobion.algebrain.support.DefaultEquationSupport;
import com.obdobion.argument.annotation.Arg;
import com.obdobion.calendar.TemporalHelper;
import com.obdobion.howto.Context;
import com.obdobion.howto.IPluginCommand;
import com.obdobion.howto.Outline;

/**
 * <p>
 * Calculator class.
 * </p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class Calculator implements IPluginCommand
{
    /** Constant <code>GROUP="Utility"</code> */
    static final public String           GROUP = "Utility";
    /** Constant <code>NAME="calc"</code> */
    static final public String           NAME  = "calc";

    @Arg(positional = true,
            caseSensitive = true,
            help = "An algebraic equation that will be solved.  Quotes around the entire equation are usually necessary since special math characters are also special to command line arguments in general.")
    private Equ                          equation;

    @Arg(longName = "rpn",
            shortName = 'r',
            help = "Shows the \"reverse polish notation\" for the previously evaluated equation.")
    private boolean                      showRPN;

    @Arg(shortName = 'd',
            allowCamelCaps = true,
            defaultValues = "3",
            help = "The number of decimal places to show for floating point results.")
    private int                          decimalPlaces;

    @Arg(help = "All stored memory (variables) will be cleared.")
    private boolean                      clear;

    @Arg(allowCamelCaps = true,
            help = "Show Memory")
    private boolean                      showMemory;

    private final DefaultEquationSupport equationVariables;

    /**
     * <p>
     * Constructor for Calculator.
     * </p>
     */
    public Calculator()
    {
        equationVariables = new DefaultEquationSupport();
    }

    /** {@inheritDoc} */
    @Override
    public int execute(final Context context) throws ParseException
    {
        final Outline message = context.getOutline();
        try
        {
            equation.setSupport(equationVariables);

            if (clear)
                equationVariables.clear();
            else if (showMemory)
                showMemory(message, null);
            else if (showRPN)
                showRPN(message);
            else
                showEvaluation(message);

        } catch (final Exception e)
        {
            throw new ParseException(equation.toString() + ": " + e.getMessage(), 0);
        }
        return 0;
    }

    void formatVariable(final Outline message, final int longestVarName, final String varName, final Object value)
    {
        if (value instanceof Long)
            message.printf("%1$" + longestVarName + "s %2$d",
                    varName, ((Long) value).longValue());
        else if (value instanceof Double)
            message.printf("%1$" + longestVarName + "s %2$." + decimalPlaces + "f",
                    varName, ((Double) value).doubleValue());
        else if (value instanceof Boolean)
            message.printf("%1$" + longestVarName + "s %2$s",
                    varName, ((Boolean) value).toString());
        else if (value instanceof LocalDateTime)
        {
            final LocalDateTime ldt = (LocalDateTime) value;
            if (ldt.toLocalDate() == LocalDate.MIN)
                message.printf("%1$" + longestVarName + "s %2$s",
                        varName, TemporalHelper.getOutputTF().format(ldt.toLocalTime()));
            else if (ldt.toLocalTime() == LocalTime.MIN)
                message.printf("%1$" + longestVarName + "s %2$s",
                        varName, TemporalHelper.getOutputDF().format(ldt.toLocalDate()));
            else
                message.printf("%1$" + longestVarName + "s %2$s",
                        varName, TemporalHelper.getOutputDTF().format(ldt));

        } else
            message.printf("%1$" + longestVarName + "s %2$s",
                    varName, value.toString());

        message.printf("\n");
    }

    /** {@inheritDoc} */
    @Override
    public String getGroup()
    {
        return GROUP;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public String getOverview()
    {
        return "You can use this command to try out anything you can do with the Algebrain package.  Use this link to read about it.  https://github.com/fedups/com.obdobion.algebrain/wiki";
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOnceAndDone()
    {
        return false;
    }

    private void showEvaluation(final Outline message) throws Exception
    {
        showMemory(message, equation.evaluate());
    }

    private void showMemory(final Outline message, final Object result) throws Exception
    {
        message.printf("\n");
        final List<String> varNames = equationVariables.getVariableNames(false);
        varNames.sort((s1, s2) -> s1.compareTo(s2));

        int longestVarName = 7;
        for (final String varName : varNames)
        {
            longestVarName = Math.max(longestVarName, varName.length());
        }
        for (final String varName : varNames)
        {
            final Object value = equationVariables.resolveVariable(varName);
            formatVariable(message, longestVarName, varName, value);
        }
        if (result != null)
            formatVariable(message, longestVarName, "RESULT", result);
        message.printf("\n");
    }

    private void showRPN(final Outline message) throws Exception
    {
        message.printf("\n");
        message.printf(equation.showRPN());
        message.printf("\n");
    }
}
