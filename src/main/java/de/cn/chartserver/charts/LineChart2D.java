package de.cn.chartserver.charts;

import com.google.gson.Gson;

public class LineChart2D extends Chart{
    protected final String PATH ="/linechart2d";
    
    protected double[] data;
    
    public LineChart2D() {
        super();
    }
    
    public LineChart2D(int port) {
        super(port);
    }
    
    public LineChart2D(String host,int port) {
        super( host, port);
    }
    
    public String draw(double[] yvalues){
        this.data = yvalues;

        return super.send(PATH,(new Gson()).toJson(new DTOLineChart2D(yvalues)));
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }
}
