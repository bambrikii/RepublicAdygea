package ru.sovzond.mgis2.rusregister.kpt.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class SpelementUnit {
	private String typeUnit;
	private int subNumb;
	private List<Ordinate> ordinates = new ArrayList<>();

	public String getTypeUnit() {
		return typeUnit;
	}

	public void setTypeUnit(String typeUnit) {
		this.typeUnit = typeUnit;
	}

	public int getSubNumb() {
		return subNumb;
	}

	public void setSubNumb(int subNumb) {
		this.subNumb = subNumb;
	}

	public List<Ordinate> getOrdinates() {
		return ordinates;
	}

	public void setOrdinates(List<Ordinate> ordinates) {
		this.ordinates = ordinates;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("typeUnit", typeUnit).append("subNumb", subNumb).append("ordinates", ordinates).toString();
	}
}
