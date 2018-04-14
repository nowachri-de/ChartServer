package de.cn.chartserver.charts.dto;

import com.google.gson.Gson;

import de.cn.chartserver.charts.LineChart2D;

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
    
    /** 
     * Returns a JSON representation of object
     * 
     * @return JSON representation of object
     */
    @Override
    public String toString(){
		return (new Gson()).toJson(this);
    }
}
