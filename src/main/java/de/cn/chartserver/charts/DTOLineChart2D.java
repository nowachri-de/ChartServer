package de.cn.chartserver.charts;

/**
 * @author Christian Nowak
 *
 */
public class DTOLineChart2D {
    protected final String CHART_TYPE= LineChart2D.class.getSimpleName();
    double[] data;
    public DTOLineChart2D(double data[]) {
        this.data = data;
    }

}
