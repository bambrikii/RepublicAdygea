package ru.sovzond.mgis2.rusregister.kpt.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class EntitySpatial {
	private String entSys;
	private List<SpatialElement> spatialElements = new ArrayList<>();

	public String getEntSys() {
		return entSys;
	}

	public void setEntSys(String entSys) {
		this.entSys = entSys;
	}

	public List<SpatialElement> getSpatialElements() {
		return spatialElements;
	}

	public void setSpatialElements(List<SpatialElement> spatialElements) {
		this.spatialElements = spatialElements;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("entSys", entSys).append("spatialElements", spatialElements).toString();
	}
}
