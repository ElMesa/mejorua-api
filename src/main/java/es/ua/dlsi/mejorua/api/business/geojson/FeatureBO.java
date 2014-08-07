package es.ua.dlsi.mejorua.api.business.geojson;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FeatureBO {
	
	private String type;
	private HashMap<String,Object> geometry;
	private HashMap<String,String> properties;

	public FeatureBO() {
		type = "Feature";
		geometry = new HashMap<String, Object>();
		geometry.put("type", "Point");
		geometry.put("coordinates", new double[]{0.0, 0.0});
		properties = new HashMap<String, String>();
	}
	
	@JsonIgnore
	public double getLatitude() {
		double[] coordinates = (double[]) geometry.get("coordinates");
		return coordinates[1];
	}
	
	public void setLatitude(double latitude) {
		double[] coordinates = (double[]) geometry.get("coordinates");
		coordinates[1] = latitude;
	}
	
	@JsonIgnore
	public double getLongitude() {
		double[] coordinates = (double[]) geometry.get("coordinates");
		return coordinates[0];
	}
	
	public void setLongitude(double longitude) {
		double[] coordinates = (double[]) geometry.get("coordinates");
		coordinates[0] = longitude;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
	
	public String getProperty(String propierty) {
		return properties.get(propierty);
	}

	public void setProperty(String key, String value) {
		properties.put(key, value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String,Object> getGeometry() {
		return geometry;
	}
	
	public void setGeometry(HashMap<String,Object> geometry) {
		this.geometry = geometry;
	}
}
