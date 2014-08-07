package es.ua.dlsi.mejorua.api.business;

import es.ua.dlsi.mejorua.api.transfer.IssueTO.State;

public class IssueEventBO {

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// OWN DATA
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public enum eventType {
		create, state_change_pending, state_change_inProgress, state_change_done
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// ATRIBUTTES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	private long id;
	private eventType type;
	private long date;

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// METHODS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public IssueEventBO(long id, eventType type) {
		this.id = id;
		date = System.currentTimeMillis();

		this.type = type;
	}

	public static IssueEventBO newChangeState(long id, State state) {

		IssueEventBO issue = null;
		boolean isKnownState = true;
		eventType type = null;

		switch (state) {
		case pending:
			type = eventType.state_change_pending;
			break;

		case inProgress:
			type = eventType.state_change_inProgress;
			break;

		case done:
			type = eventType.state_change_done;
			break;

		default:
			isKnownState = false;
			break;
		}

		if (isKnownState) {
			issue = new IssueEventBO(id, type);
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
}
