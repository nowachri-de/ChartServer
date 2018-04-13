package de.cn.chartserver.charts;

import com.google.gson.Gson;

/**
 * Class representing a 2D LineChart.
 *
 */
public class LineChart2D extends Chart{
    protected final String PATH ="/linechart2d";
    
    protected double[] data;
    
    /**
     * No arg constructor
     */
    public LineChart2D() {
        super();
    }
    
    /**
     * Constructor expecting the server's listening port as parameter.
     * 
     * @param port Port at which server is listening
     */
    public LineChart2D(int port) {
        super(port);
    }
    
    /**
     * Constructor expecting the server's hostname and listening port as parameter.
     * 
     * @param host Hostname of server
     * @param port Port at which server is listening
     */
    public LineChart2D(String host,int port) {
        super( host, port);
    }

    
    /**
     * Request the server to draw the chart on the browser. Sounds wired but is realised
     * utilizing a web socket.
     * 
     * @param yvalues Y values of the 2D line chart
     * @return Response of the server
     */
    public String draw(double[] yvalues){
        this.data = yvalues;

        return super.send(PATH,new DTOLineChart2D(this.data));
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }
}
