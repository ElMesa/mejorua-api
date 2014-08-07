package es.ua.dlsi.mejorua.api.business;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.ua.dlsi.mejorua.api.business.geojson.FeatureBO;
import es.ua.dlsi.mejorua.api.persistance.IIssueDAO;
import es.ua.dlsi.mejorua.api.persistance.IssueDAO;

@Entity
public class IssueBO {

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

	private static IIssueDAO dao;
	private IssueEventCollection events;
	private FeatureBO geoJSONFeature; // Derived atribute - IssueBO JSON
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

	// Static constructor
	static {
		dao = new IssueDAO();
	}

	// Constructor
	public IssueBO() {

		geoJSONFeature = new FeatureBO();

		onCreate();
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DAO
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public static HashMap<Long, IssueBO> getAll() {
		return dao.getAll();
	}

	public static IssueBO get(long id) {
		return dao.get(id);
	}

	public static long add(IssueBO issue) {

		long newId = -1;

		if (issue.isValid()) {
			newId = dao.create(issue);
		}

		return newId;
	}

	public boolean update() {

		boolean isSaved = false;

		if (isValid() && get(id) != null) {
			dao.update(this);
			isSaved = true;
		}

		return isSaved;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// EVENTS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public void onCreate() {

		IssueEventBO event;

		this.events = new IssueEventCollection();
		event = this.events.create();
		this.creationDate = event.getDate();
	}

	public boolean onChangeState(State state) {

		IssueEventBO event;
		boolean isChanged = false;

		if (setState(state)) {
			event = events.changeState(state);
			this.lastModifiedDate = event.getDate();
			isChanged = true;
		}

		return isChanged;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// VALIDATORS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	// TODO Implement this mockup validation and subvalidation using
	// util.Validator and giving meaningfull error responses for each fail
	// (wachtout security issues with the info given)
	@JsonIgnore
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

	@JsonIgnore
	public boolean isValidId() {
		boolean isValid = false;

		if (id > 0)
			isValid = true;

		return isValid;
	}

	@JsonIgnore
	public boolean isValidLatitude() {
		boolean isValid = true;
		return isValid;
	}

	@JsonIgnore
	public boolean isValidLongitude() {
		boolean isValid = true;
		return isValid;
	}

	@JsonIgnore
	public boolean isValidAction() {
		boolean isValid = true;
		return isValid;
	}

	@JsonIgnore
	public boolean isValidTerm() {
		boolean isValid = true;
		return isValid;
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
		// this.geoJSONFeature.setId(String.valueOf(this.id));
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

	public boolean setState(State state) {

		boolean isSetted = false;

		if (this.state != state) {
			this.state = state;
			this.geoJSONFeature.setProperty("state", this.state.name());

			isSetted = true;
		}

		return isSetted;
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

}
