package es.ua.dlsi.mejorua.api.transfer;

import java.util.ArrayList;
import java.util.HashMap;
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
//@IdClass(class=SingleFieldIdentity.class)
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

	@OneToMany(orphanRemoval = true)
	//@JoinColumn(name = "ISSUE_ID", referencedColumnName="ID")
	@JoinColumn(name = "ISSUEID")
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
	private String idSIGUA;

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
	//@OneToMany(orphanRemoval = true)
	//@JoinColumn(name = "ISSUE_ID", referencedColumnName="ID")
	//@JoinColumn(name = "ISSUE_ID")
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

	public String getIdSIGUA() {
		return idSIGUA;
	}

	public void setIdSIGUA(String idSIGUA) {
		this.idSIGUA = idSIGUA;
	}
	
	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEBUG
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public static List<IssueTO> DEBUGnewIssueList() {

		ArrayList<IssueTO> issues = new ArrayList<IssueTO>();
		
//		float[] aulario2Coords = new float[] { 38.384488f, -0.510120f };
//		float[] bibliotecaGeneralCoords = new float[] { 38.383235f, -0.512158f };
//		float[] eps1Coords = new float[] { 38.386755f, -0.511295f };
//		float[] estanquePatos = new float[] { 38.385632f, -0.518701f };
//
//		issues.add(DEBUGnewIssue(1, State.PENDING, aulario2Coords));
//		issues.add(DEBUGnewIssue(2, State.DONE, bibliotecaGeneralCoords));
//		issues.add(DEBUGnewIssue(3, State.INPROGRESS, eps1Coords));
		
		HashMap<String,String> A2SalonActos = new HashMap<String,String>();
		A2SalonActos.put("id", "1");
		A2SalonActos.put("name", "A2SalonActos");
		A2SalonActos.put("state", "PENDING");
		A2SalonActos.put("latitude", "38.38443437317912");
		A2SalonActos.put("longitude", "-0.5099328607320786");
		A2SalonActos.put("idSIGUA", "0030PB010");
		
		HashMap<String,String> BibliotecaGeneralPatio = new HashMap<String,String>();
		BibliotecaGeneralPatio.put("id", "2");
		BibliotecaGeneralPatio.put("name", "BibliotecaGeneralPatio");
		BibliotecaGeneralPatio.put("state", "DONE");
		BibliotecaGeneralPatio.put("latitude", "38.38325171727514");
		BibliotecaGeneralPatio.put("longitude", "-0.5119830742478371");
		BibliotecaGeneralPatio.put("idSIGUA", "0033PB040");

		HashMap<String,String> EPS1LibreAcceso = new HashMap<String,String>();
		EPS1LibreAcceso.put("id", "3");
		EPS1LibreAcceso.put("name", "EPS1LibreAcceso");
		EPS1LibreAcceso.put("state", "INPROGRESS");
		EPS1LibreAcceso.put("latitude", "38.386904221968365");
		EPS1LibreAcceso.put("longitude", "-0.511702112853527");
		EPS1LibreAcceso.put("idSIGUA", "0016PB054");
		
		issues.add(DEBUGnewIssueFromHash(A2SalonActos));
		issues.add(DEBUGnewIssueFromHash(BibliotecaGeneralPatio));
		issues.add(DEBUGnewIssueFromHash(EPS1LibreAcceso));
		
		return issues;
	}

//	private static IssueTO DEBUGnewIssue(int id, State state, float[] coordinates) {
//		IssueBO issueBO = new IssueBO();
//		IssueTO issueTO = issueBO.getTO();
//
//		issueTO.setId(id);
//		issueTO.setState(state);
//		issueTO.setTerm(id + " PrePopulated Term");
//		issueTO.setAction(id + " PrePopulated Action");
//
//		issueTO.setLatitude(coordinates[0]);
//		issueTO.setLongitude(coordinates[1]);
//
//		return issueTO;
//	}
	
	private static IssueTO DEBUGnewIssueFromHash(HashMap<String,String> data) {
		IssueBO issueBO = new IssueBO();
		IssueTO issueTO = issueBO.getTO();

		issueTO.setId(Integer.valueOf(data.get("id")));
		issueTO.setState(State.valueOf(data.get("state")));
		issueTO.setTerm(data.get("id") + "-" + data.get("name") + " - Term");
		issueTO.setAction(data.get("id") + "-" + data.get("name") + " - Action");

		issueTO.setLatitude(Double.valueOf(data.get("latitude")));
		issueTO.setLongitude(Double.valueOf(data.get("longitude")));
		issueTO.setIdSIGUA(data.get("idSIGUA"));

		return issueTO;
	}

}
