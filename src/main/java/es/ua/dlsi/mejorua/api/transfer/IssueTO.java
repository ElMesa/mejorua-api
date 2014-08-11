package es.ua.dlsi.mejorua.api.transfer;

import javax.persistence.Basic;
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
/*
	public enum State {
		pending, inProgress, done
	}
*/
	//JPA Friendly enum - Modification safe (As stated here: http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in-jpa.html) 
	public enum State {

	    PENDING(1),
	    INPROGRESS(2),
	    DONE(3);
	    
	    private int id;   

	    private State(int id) {
	        this.id = id;
	    }

	    public static State getType(Integer id) {
	      
	        if (id == null) {
	            return null;
	        }

	        for (State state : State.values()) {
	            if (id.equals(state.getId())) {
	                return state;
	            }
	        }
	        throw new IllegalArgumentException("es.ua.dlsi.mejorua.api.transfer.IssueTO.State - No matching type for id " + id);
	    }

	    public int getId() {
	        return id;
	    }
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

	@Basic
	private Integer state;

	@Basic
	private String action;
	@Basic
	private String term;

	@Basic
	private double latitude;
	@Basic
	private double longitude;

	@Basic
	private long creationDate; // Derived attribute - From first create event
								// date in events
	@Basic
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
		return State.getType(this.state);
	}

	public void setState(State state) {
		if (state == null) {
			this.state = null;
			this.geoJSONFeature.setProperty("state", null);
        } else {
        	this.state = state.getId();
        	this.geoJSONFeature.setProperty("state", State.getType(this.state).name());
        }
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
