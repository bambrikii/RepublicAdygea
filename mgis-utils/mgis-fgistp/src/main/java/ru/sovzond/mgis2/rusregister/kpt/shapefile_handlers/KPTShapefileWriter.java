package ru.sovzond.mgis2.rusregister.kpt.shapefile_handlers;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.primitive.Point;
import ru.sovzond.mgis2.rusregister.kpt.model.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class KPTShapefileWriter {
	private String directory;
	private String fileName;

	private SimpleFeatureType TYPE = null;
	private ShapefileDataStore newDataStore = null;
	private LiteCoordinateSequenceFactory coordinateSequenceFactory;

	public KPTShapefileWriter(String directory, String fileName) {
		this.directory = directory;
		this.fileName = fileName;
		coordinateSequenceFactory = new LiteCoordinateSequenceFactory();

		try {
			TYPE = DataUtilities.createType("Location",
					"" //
							+ "cadastral_number:String," // <- a String attribute
							+ "state:String," // a number attribute
							+ "date_created:String," //
							+ "area:String," //
							+ "area_unit:String," //
							+ "name:String," //
							+ "location_in_bounds:String," //
							+ "address_okato:String," //
							+ "address_kladr:String," //
							+ "address_region:String," //
							+ "address_district_name:String," //
							+ "address_district_type:String," //
							+ "address_city_name:String," //
							+ "address_city_type:String," //
							+ "address_locality_name:String," //
							+ "address_locality_type:String," //
							+ "address_street_name:String," //
							+ "address_street_type:String," //
							+ "address_level_1_type:String," //
							+ "address_level_1_value:String," //
							+ "addess_note:String," //
							+ "category:String," //
							+ "utilization:String," //
							+ "utilization_by_doc:String," //
							+ "cadastral_cost:String," //
							+ "cadastral_unit:String," //
							+ "location:Polygon:srid=4326" // <- the geometry attribute: Point type
			);
		} catch (SchemaException e) {
			e.printStackTrace();
		}

		File newFile = new File(directory + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + ".shp");

		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();


		Map<String, Serializable> params = new HashMap<>();
		try {
			params.put("url", newFile.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		params.put("create spatial index", Boolean.TRUE);

		try {
			newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			newDataStore.createSchema(TYPE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Parcel parcel) {
		System.out.println(parcel);
		/*
		 * We create a FeatureCollection into which we will put each Feature created from a record
         * in the input csv data file
         */
		DefaultFeatureCollection collection = new DefaultFeatureCollection("internal", TYPE);

		/*
		 * GeometryFactory will be used to create the geometry attribute of each feature (a Point
         * object for the location)
         */
//		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(TYPE);
		builder.add(parcel.getCadastralNumber());
		builder.add(parcel.getState());
		builder.add(parcel.getDateCreated());
		builder.add(parcel.getArea());
		builder.add(parcel.getAreaUnit());
		builder.add(parcel.getName());
		builder.add(parcel.getLocationInBounds());
		Address address = parcel.getAddress();
		builder.add(address.getOkato());
		builder.add(address.getKladr());
		builder.add(address.getRegion());
		builder.add(address.getDistrictName());
		builder.add(address.getDistrictType());
		builder.add(address.getCityName());
		builder.add(address.getCityType());
		builder.add(address.getLocalityName());
		builder.add(address.getLocalityType());
		builder.add(address.getStreetName());
		builder.add(address.getStreetType());
		builder.add(address.getLevel1Value());
		builder.add(address.getNote());
		builder.add(parcel.getCategory());
		builder.add(parcel.getUtilization());
		builder.add(parcel.getUtilizationByDoc());
		builder.add(parcel.getCadastralCost());
		builder.add(parcel.getCadastralUnit());


		if (parcel.getEntitySpatial() != null && parcel.getEntitySpatial().getSpatialElements() != null && parcel.getEntitySpatial().getSpatialElements().size() > 0) {
			GeometryBuilder geometryBuilder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
			PointArray points = null;
//			List<Point> points = new ArrayList<>();
			for (SpatialElement spatialElement : parcel.getEntitySpatial().getSpatialElements()) {
				for (SpelementUnit spelementUnit : spatialElement.getSpelementUnits()) {
					for (Ordinate ordinate : spelementUnit.getOrdinates()) {
						double longitude = ordinate.getX();
						double latitude = ordinate.getY();
						int number = ordinate.getOrdNmb();
						String name = String.valueOf(number) + String.valueOf(ordinate.getDeltaGeopoint());

						/* Longitude (= x coord) first ! */
						Point point = geometryBuilder.createPoint(new double[]{longitude, latitude});
						points.add(number, point);
					}
					SimpleFeature feature = builder.buildFeature(null);
					collection.add(feature);
				}
			}
			builder.add(geometryBuilder.createPolygon(geometryBuilder.createSurfaceBoundary(points)));
		}


		/*
		 * Write the features to the shapefile
         */
		Transaction transaction = new DefaultTransaction("create");

		String typeName = null;
		try {
			typeName = newDataStore.getTypeNames()[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
		SimpleFeatureSource featureSource = null;
		try {
			featureSource = newDataStore.getFeatureSource(typeName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

			featureStore.setTransaction(transaction);
			try {
				featureStore.addFeatures(collection);
				transaction.commit();

			} catch (Exception problem) {
				problem.printStackTrace();
				try {
					transaction.rollback();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} finally {
				try {
					transaction.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println(typeName + " does not support read/write access");
		}
	}

	public void m1() {

	}
}
