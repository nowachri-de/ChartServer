package de.cn.chartserver.charts;

import java.net.URI;

import org.pmw.tinylog.Logger;

import de.cn.chartserver.charts.dto.DTOLineChart2D;

/**
 * Class representing a 2D LineChart.
 *
 */
public class LineChart2D extends Chart {
	protected final String PATH = "/linechart2d";

	protected double[] data;

	public LineChart2D(URI uri) {
		super(uri);
	}

	public void draw(double[] yvalues) {
		this.data = yvalues;
		try {
			this.connectBlocking();
		} catch (InterruptedException e) {
			Logger.error(e);
			return;
		}
		this.send((new DTOLineChart2D(data)).toString());
		this.close();
	}

	public double[] getData() {
		return data;
	}

}
