package es.ua.dlsi.mejorua.api.business.geojson;

import org.geojson.LngLatAlt;
import org.geojson.Point;

public class PointBO extends Point{

	private String type;
	
	public PointBO(LngLatAlt lngLatAlt) {
		super(lngLatAlt);
		type = "Point";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setLatitude(double latitude) {
		LngLatAlt lnglat = this.getCoordinates();
		lnglat.setLatitude(latitude);
		this.setCoordinates(lnglat);
	}
	
	public void setLongitude(double longitude) {
		LngLatAlt lnglat = this.getCoordinates();
		lnglat.setLongitude(longitude);
		this.setCoordinates(lnglat);
	}
}
