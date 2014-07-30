package es.ua.dlsi.mejorua.api.business;

import java.util.HashMap;

import es.ua.dlsi.mejorua.api.business.geojson.FeatureBO;
import es.ua.dlsi.mejorua.api.persistance.IssueDAO;

public class IssueBO {

	private long id;
	private String status;
	private String action;
	private String term;
	
	private double latitude;
	private double longitude;
	private FeatureBO geoJSONFeature;
	
	public IssueBO() {
		this.geoJSONFeature = new FeatureBO();
	}

	public static HashMap<Long, IssueBO> getAll() {
		return IssueDAO.getAll();
	}
	
	public static IssueBO get(long id) {
		return IssueDAO.get(id);
	}

	public static long add(IssueBO issue) {
		
		long newId = -1;
		
		if (issue.isValid()) {
			newId = IssueDAO.add(issue);
		}
		
		return newId;
	}

	public boolean update() {

		boolean isSaved = false;

		if (isValid() && get(id) != null) {
			IssueDAO.update(this);
			isSaved = true;
		}

		return isSaved;
	}

	// ///////////////////////
	//
	// Validators
	//
	// ///////////////////////

	// TODO Implement this mockup validation and subvalidation using
	// util.Validator and giving meaningfull error responses for each fail
	// (wachtout security issues with the info given)
	public boolean isValid() {

		boolean isValid = true;

		if (isValidId())
			if (isValidLatitude())
				if (isValidLongitude())
					if (isValidAction())
						if (isValidTerm())
							isValid = true;

		return isValid;
	}

	public boolean isValidId() {
		boolean isValid = false;

		if (id > 0)
			isValid = true;

		return isValid;
	}

	public boolean isValidLatitude() {
		boolean isValid = true;
		return isValid;
	}

	public boolean isValidLongitude() {
		boolean isValid = true;
		return isValid;
	}

	public boolean isValidAction() {
		boolean isValid = true;
		return isValid;
	}

	public boolean isValidTerm() {
		boolean isValid = true;
		return isValid;
	}

	// ///////////////////////
	//
	// GETTERS AND SETTERS
	//
	// ///////////////////////

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		//this.geoJSONFeature.setId(String.valueOf(this.id));
		this.geoJSONFeature.setProperty("id", String.valueOf(this.id));
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		this.geoJSONFeature.setLatitude(latitude);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		this.geoJSONFeature.setLongitude(longitude);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
		this.geoJSONFeature.setProperty("action", this.action);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
		this.geoJSONFeature.setProperty("term", this.term);
	}

	public FeatureBO getGeoJSONFeature() {
		return geoJSONFeature;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.geoJSONFeature.setProperty("status", this.status);
	}
}
