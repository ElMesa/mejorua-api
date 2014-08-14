package es.ua.dlsi.mejorua.api.transfer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.business.IssueEventBO;
import es.ua.dlsi.mejorua.api.business.geojson.FeatureBO;

@Entity
public class IssueTO {

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// OWN DATA
	//
	// /////////////////////////////////////////////////////////////////////////////////
	/*
	 * public enum State { pending, inProgress, done }
	 */
	// JPA Friendly enum - Modification safe (As stated here:
	// http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in-jpa.html)
	public enum State {

		PENDING(1), INPROGRESS(2), DONE(3);

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
			throw new IllegalArgumentException(
					"es.ua.dlsi.mejorua.api.transfer.IssueTO.State - No matching type for id "
							+ id);
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

	private List<IssueEventBO> events;
	private FeatureBO geoJSONFeature; // Derived atribute - IssueBO in geoJSON
										// Notation

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// ATRIBUTTES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
		events = new ArrayList<IssueEventBO>();
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
			this.geoJSONFeature.setProperty("state", State.getType(this.state)
					.name());
		}
	}

	
	@JsonProperty(value = "events")
	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "ISSUE_EVENT_ID")
	public List<IssueEventBO> getEvents() {
		return events;
	}
	
	public void setEvents(List<IssueEventBO> events) {
		this.events = events;
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

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEBUG
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public static List<IssueTO> DEBUGnewIssueList() {

		ArrayList<IssueTO> issues = new ArrayList<IssueTO>();
		
		float[] aulario2Coords = new float[] { 38.384488f, -0.510120f };
		float[] bibliotecaGeneralCoords = new float[] { 38.383235f, -0.512158f };
		float[] eps1Coords = new float[] { 38.386755f, -0.511295f };
		float[] estanquePatos = new float[] { 38.385632f, -0.518701f };

		issues.add(DEBUGnewIssue(1, State.PENDING, aulario2Coords));
		issues.add(DEBUGnewIssue(2, State.DONE, bibliotecaGeneralCoords));
		issues.add(DEBUGnewIssue(3, State.INPROGRESS, eps1Coords));

		return issues;
	}

	private static IssueTO DEBUGnewIssue(int id, State state, float[] coordinates) {
		IssueBO issueBO = new IssueBO();
		IssueTO issueTO = issueBO.getTO();

		issueTO.setId(id);
		issueTO.setState(state);
		issueTO.setTerm(id + " PrePopulated Term");
		issueTO.setAction(id + " PrePopulated Action");

		issueTO.setLatitude(coordinates[0]);
		issueTO.setLongitude(coordinates[1]);

		return issueTO;
	}

}
