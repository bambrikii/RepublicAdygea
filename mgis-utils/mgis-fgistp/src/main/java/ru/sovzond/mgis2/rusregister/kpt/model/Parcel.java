package ru.sovzond.mgis2.rusregister.kpt.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class Parcel {
	private String cadastralNumber;
	private String state;
	private Date dateCreated;
	private String area;
	private String areaUnit;
	private String name;
	private int locationInBounds;
	private Address address;
	private String category;
	private String utilization;
	private String utilizationByDoc;
	private double cadastralCost;
	private String cadastralUnit;
	private EntitySpatial entitySpatial;

	public String getCadastralNumber() {
		return cadastralNumber;
	}

	public void setCadastralNumber(String cadastralNumber) {
		this.cadastralNumber = cadastralNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaUnit() {
		return areaUnit;
	}

	public void setAreaUnit(String areaUnit) {
		this.areaUnit = areaUnit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLocationInBounds() {
		return locationInBounds;
	}

	public void setLocationInBounds(int locationInBounds) {
		this.locationInBounds = locationInBounds;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUtilization() {
		return utilization;
	}

	public void setUtilization(String utilization) {
		this.utilization = utilization;
	}

	public String getUtilizationByDoc() {
		return utilizationByDoc;
	}

	public void setUtilizationByDoc(String utilizationByDoc) {
		this.utilizationByDoc = utilizationByDoc;
	}

	public double getCadastralCost() {
		return cadastralCost;
	}

	public void setCadastralCost(double cadastralCost) {
		this.cadastralCost = cadastralCost;
	}

	public String getCadastralUnit() {
		return cadastralUnit;
	}

	public void setCadastralUnit(String cadastralUnit) {
		this.cadastralUnit = cadastralUnit;
	}

	public EntitySpatial getEntitySpatial() {
		return entitySpatial;
	}

	public void setEntitySpatial(EntitySpatial entitySpatial) {
		this.entitySpatial = entitySpatial;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("entitySpatial", entitySpatial).toString();
	}
}
