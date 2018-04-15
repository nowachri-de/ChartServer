package de.cn.chartserver.charts;

import java.net.URI;

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
			this.send((new DTOLineChart2D(data)).toString());
			this.closeBlocking();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public double[] getData() {
		return data;
	}

}
