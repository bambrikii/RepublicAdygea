package ru.sovzond.mgis2.rusregister.kpt.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class Ordinate {
	private double x;
	private double y;
	private int ordNmb;
	private double deltaGeopoint;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getOrdNmb() {
		return ordNmb;
	}

	public void setOrdNmb(int ordNmb) {
		this.ordNmb = ordNmb;
	}

	public double getDeltaGeopoint() {
		return deltaGeopoint;
	}

	public void setDeltaGeopoint(double deltaGeopoint) {
		this.deltaGeopoint = deltaGeopoint;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("x", x).append("y", y).append("ordNmb", ordNmb).append("deltaGeopoint", deltaGeopoint).toString();
	}
}
