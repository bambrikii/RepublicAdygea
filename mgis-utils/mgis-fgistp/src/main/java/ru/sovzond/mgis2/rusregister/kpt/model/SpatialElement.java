package ru.sovzond.mgis2.rusregister.kpt.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class SpatialElement {
	private List<SpelementUnit> spelementUnits = new ArrayList<>();

	public List<SpelementUnit> getSpelementUnits() {
		return spelementUnits;
	}

	public void setSpelementUnits(List<SpelementUnit> spelementUnits) {
		this.spelementUnits = spelementUnits;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("spelementUnits", spelementUnits).toString();
	}
}
