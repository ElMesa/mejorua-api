package es.ua.dlsi.mejorua.api.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;

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
	long issueId;

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// ATRIBUTTES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	@Id
	@GeneratedValue
	//@Column(name = "ISSUE_EVENT_ID")
	private long id;
	
	//@Basic
	//@Column(name = "ISSUE_ID")
	//mvn private long idIssue;
	
	@Basic
	private eventType type;
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
		this.issueId = issueId;
		date = System.currentTimeMillis();

		this.type = type;
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
		return type;
	}

	public void setType(eventType type) {
		this.type = type;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getIssueId() {
		return issueId;
	}

	public void setIssueId(long issueId) {
		this.issueId = issueId;
	}

}
