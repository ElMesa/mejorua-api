package es.ua.dlsi.mejorua.api.transfer;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.ua.dlsi.mejorua.api.business.IssueEventCollection;
import es.ua.dlsi.mejorua.api.business.geojson.FeatureBO;

@Entity
public class IssueTO {

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// OWN DATA
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public enum State {
		pending, inProgress, done
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEPENDENCIES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	private IssueEventCollection events;
	private FeatureBO geoJSONFeature; // Derived atribute - IssueBO in geoJSON
										// Notation

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// ATRIBUTTES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	@Id
	private long id;

	private State state;

	private String action;
	private String term;

	private double latitude;
	private double longitude;

	private long creationDate; // Derived attribute - From first create event
								// date in events
	private long lastModifiedDate; // Derived attribute - From last event date
									// in events

	// SIGUACODE de asocia issue con localizacion "logico"

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	// Constructor
	public IssueTO() {
		geoJSONFeature = new FeatureBO();
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// GETTERS AND SETTERS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		this.geoJSONFeature.setProperty("state", this.state.name());
	}

	@JsonIgnore
	public IssueEventCollection getEvents() {
		return events;
	}

	public void setEvents(IssueEventCollection events) {
		this.events = events;
	}

	@JsonProperty(value = "events")
	public Object[] getEventsList() {
		return events.getEvents().toArray();
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public long getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	
}
