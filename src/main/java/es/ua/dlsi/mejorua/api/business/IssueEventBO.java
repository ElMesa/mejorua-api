package es.ua.dlsi.mejorua.api.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import es.ua.dlsi.mejorua.api.transfer.IssueTO.State;

@Entity
//@IdClass(class=SingleFieldIdentity.class)
public class IssueEventBO {

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// OWN DATA
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public enum eventType {
		CREATE(1), STATE_CHANGE_PENDING(2), STATE_CHANGE_INPROGRESS(3), STATE_CHANGE_DONE(
				4);

		private int id;

		private eventType(int id) {
			this.id = id;
		}

		public static eventType getType(Integer id) {

			if (id == null) {
				return null;
			}

			for (eventType type : eventType.values()) {
				if (id.equals(type.getId())) {
					return type;
				}
			}
			throw new IllegalArgumentException(
					"es.ua.dlsi.mejorua.api.business.IssueEventBO.eventType - No matching type for id "
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

	// {IssueTO} id
	//long issueId;

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// ATRIBUTTES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	@Id
	@GeneratedValue
	private long id;
	
	//Commented because: Using unidirectional @oneToMany 
	//@Basic
	//@Column(name = "ISSUE_ID")
	//private long idIssue;
	
	@Basic
	//@Transient
	//private eventType type;
	private Integer type;
	
	@Basic
	private long date;

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// STATIC METHODS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public static long lastEventDate(List<IssueEventBO> events) {
		long lastDate = -1;

		if (events != null && events.size() > 0) {
			lastDate = events.get(events.size() - 1).getDate();
		}

		return lastDate;
	}

	public static List<IssueEventBO> eventsAfterDate(long date,
			List<IssueEventBO> events) {
		ArrayList<IssueEventBO> filteredEvents = new ArrayList<IssueEventBO>();

		for (IssueEventBO event : events) {
			if (event.getDate() > date)
				filteredEvents.add(event);
		}

		return filteredEvents;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// METHODS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public IssueEventBO() {
	}

	public IssueEventBO(long issueId, eventType type) {
		//this.issueId = issueId;
		date = System.currentTimeMillis();

		this.setType(type);
	}

	public static IssueEventBO newChangeState(long issueId, State state) {

		IssueEventBO issue = null;
		boolean isKnownState = true;
		eventType type = null;

		switch (state) {
		case PENDING:
			type = eventType.STATE_CHANGE_PENDING;
			break;

		case INPROGRESS:
			type = eventType.STATE_CHANGE_INPROGRESS;
			break;

		case DONE:
			type = eventType.STATE_CHANGE_DONE;
			break;

		default:
			isKnownState = false;
			break;
		}

		if (isKnownState) {
			issue = new IssueEventBO(issueId, type);
		}

		return issue;
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
	}

	public eventType getType() {
		return eventType.getType(this.type);
	}
	
	public void setType(eventType type) {
		this.type = type.getId();
	}
	
	/*
	BUG - Datanucleus enhancer crashes
	when triying to persist the Enum id via this getter and setter as seen in: (http://www.datanucleus.org/products/accessplatform_3_3/jpa/fields_properties.html)
	Exception: "Class es.ua.dlsi.mejorua.api.business.IssueEventBO has application-identity and no objectid-class specified yet has [Ljava.lang.Object;@69e0b7a2 primary key fields. Unable to use SingleFieldIdentity."
	
	@Basic
	@Column(name = "TYPE")
	public int getTypeId() {
		return type.getId();
	}
	
	public void setTypeId(int typeId) {
		this.type = eventType.getType(typeId);
	}
	*/

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getIssueId() {
		//return issueId;
		return -1;
	}

	public void setIssueId(long issueId) {
		//this.issueId = issueId;
	}

}
