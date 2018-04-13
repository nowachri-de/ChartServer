package de.cn.chartserver.charts;

import com.google.gson.Gson;

/**
 * Used as kind of a DTO object between client and server.
 * Instances of this class are transformed to JSON. The JSON message is then transferred to the server.
 *
 */
public class DTOLineChart2D {
    protected final String CHART_TYPE= LineChart2D.class.getSimpleName();
    double[] data;
    
    /**
     * Constructor expecting the charts y positions as double array.
     * 
     * @param data Array of double values representing the Y Values
     */
    public DTOLineChart2D(double ypositions[]) {
        this.data = ypositions;
    }
    
    @Override
    public String toString(){
		return (new Gson()).toJson(this);
    }
}
