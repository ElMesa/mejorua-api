package es.ua.dlsi.mejorua.api.business;

import java.util.List;

import es.ua.dlsi.mejorua.api.persistance.IIssueDAO;
import es.ua.dlsi.mejorua.api.persistance.IssueJPADAO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO.State;

public class IssueBO {

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEPENDENCIES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	private static IIssueDAO dao;
	//TODO - REFACTOR - GAIN:Scalability - Extract TO from BO and make all methods static to eliminate 1to1 dependecy between them. BO doens needs to have his own internal state (the TO), just needs know how to work with TO's
	private IssueTO to;

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	// Static constructor
	static {
		//dao = new IssueDebugDAO();
		dao = new IssueJPADAO();
	}

	// Constructor
	public IssueBO() {

		to = new IssueTO();

		onCreate();
	}
	
	public IssueBO(IssueTO to) {
		this.to = to;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DAO
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public static List<IssueTO> getAll() {
		return dao.getAll();
	}

	public static IssueTO get(long id) {
		return dao.get(id);
	}

	public static long add(IssueTO issueTO) {

		long newId = -1;
		
		IssueBO issueBO = new IssueBO(issueTO);

		if (issueBO.isValid()) {
			newId = dao.create(issueTO);
		}

		return newId;
	}

	public boolean update() {

		boolean isSaved = false;

		if (isValid() && get(to.getId()) != null) {
			dao.update(to);
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

		to.setEvents(new IssueEventCollection());
		event = to.getEvents().create();
		to.setCreationDate(event.getDate());
	}

	public boolean onChangeState(State state) {

		IssueEventBO event;
		boolean isChanged = false;

		if (setState(state)) {
			event = to.getEvents().changeState(state);
			to.setLastModifiedDate(event.getDate());
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

		if (to.getId() > 0)
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

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// GETTERS AND SETTERS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public IssueTO getTO() {
		return to;
	}

	public void setTO(IssueTO to) {
		this.to = to;
	}
	
	public boolean setState(State state) {

		boolean isSetted = false;

		if (to.getState() != state) {
			to.setState(state);
			isSetted = true;
		}

		return isSetted;
	}

}
