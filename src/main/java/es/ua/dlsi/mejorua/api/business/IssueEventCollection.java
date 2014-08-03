package es.ua.dlsi.mejorua.api.business;

import java.util.ArrayList;
import java.util.List;

import es.ua.dlsi.mejorua.api.business.IssueBO.State;
import es.ua.dlsi.mejorua.api.business.IssueEventBO.eventType;

public class IssueEventCollection {

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// ATRIBUTTES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	List<IssueEventBO> events;

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// METHODS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public IssueEventCollection() {
		this.events = new ArrayList<IssueEventBO>();
	}

	public void create() {

		IssueEventBO event = new IssueEventBO(events.size() + 1,
				eventType.create);

		this.events.add(event);
	}

	public void changeState(State state) {

		IssueEventBO event = IssueEventBO.newChangeState(events.size() + 1,
				state);

		if (event != null) {
			this.events.add(event);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// GETTERS AND SETTERS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public List<IssueEventBO> getEvents() {
		return events;
	}

	public void setEvents(List<IssueEventBO> events) {
		this.events = events;
	}
}
