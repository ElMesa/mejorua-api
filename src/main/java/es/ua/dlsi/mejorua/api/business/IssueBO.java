package es.ua.dlsi.mejorua.api.business;

import java.util.List;
import java.util.logging.Logger;

import es.ua.dlsi.mejorua.api.business.IssueEventBO.eventType;
import es.ua.dlsi.mejorua.api.persistance.DAOFactory;
import es.ua.dlsi.mejorua.api.persistance.DAOFactory.DAOType;
import es.ua.dlsi.mejorua.api.persistance.IIssueDAO;
import es.ua.dlsi.mejorua.api.persistance.jdbc.mysql.IssueDAO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO.State;

public class IssueBO {

	private static Logger logger = Logger.getLogger("es.ua.dlsi.mejorua.api.business.IssueBO");

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEPENDENCIES
	//
	// /////////////////////////////////////////////////////////////////////////////////

	private static IIssueDAO dao;
	// TODO - REFACTOR - GAIN:Scalability - Extract TO from BO and make all
	// methods static to eliminate 1to1 dependecy between them. BO doens needs
	// to have his own internal state (the TO), just needs know how to work with
	// TO's
	private IssueTO to;

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	// Static constructor
	static {
		DAOFactory.setDAOType(DAOType.JPA);
		dao = DAOFactory.getIssueDAO();
	}

	// Constructor
	public IssueBO() {

		to = new IssueTO();

		onCreate();
	}

	//"Copy" constructor
	public IssueBO(IssueTO to) {
		this.to = to;
	}
	
	//Creates IssueBO from partial info (bootstraps IssueEvent collection & assings the partial data) Used to habdle IssueCollection POST
	public static IssueBO newFromPartialIssueTO(IssueTO to) {
		IssueBO issueBO = new IssueBO();
		IssueTO issueTO = issueBO.getTO();
		
		//TODO - REFACTOR - IssueTO double to Double, so here we can check for null
		//TODO - REFACTOR - Create a validation function for this check
		if(to.getAction() != null && to.getTarget() != null && /*to.getLatitude() != null && to.getLongitude() != null &&*/ to.getIdSIGUA() != null) {
	
			issueTO.setAction(to.getAction());
			issueTO.setTerm(to.getTerm());
			issueTO.setLatitude(to.getLatitude());
			issueTO.setLongitude(to.getLongitude());
			issueTO.setIdSIGUA(to.getIdSIGUA());
			issueTO.setTarget(to.getTarget());;
		} else {
			issueBO = null;
		}
		
		return issueBO;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DAO
	//
	// /////////////////////////////////////////////////////////////////////////////////
	// TODO Design decision - Decide if BO controlls DAO, or if DAO can be called directly from REST API controllers
	
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

	public IssueTO update() {

		Boolean isUpdated = false;
		IssueTO updatedIssue = null;
		String error = "";

		if (isValid()) {
			if (get(to.getId()) != null) {

				updatedIssue = dao.update(to);

				if (updatedIssue == null) {
					logger.info("IssueBO - Couldnt update");
					isUpdated = false;
				}
			} else {
				logger.info("IssueBO - The issue doesn't exist");
				isUpdated = false;
			}
		} else {
			logger.info("IssueBO - Invalid data");
			isUpdated = false;
		}

		return updatedIssue;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// EVENTS
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public IssueEventBO onCreate() {

		boolean isCreated = false;
		
		this.to.setState(State.PENDING);
		
		IssueEventBO event = new IssueEventBO(to.getId(), eventType.CREATE);

		isCreated = to.getEvents().add(event);

		if (isCreated) {
			to.setCreationDate(event.getDate());
			to.setLastModifiedDate(event.getDate());
		} else {
			event = null;
		}

		return event;
	}

	public IssueTO onChangeState(State state) {

		IssueEventBO event = null;
		IssueTO changedIssueTO = null;
		State originalState = to.getState();
		boolean isAdded = false;
		boolean isError = false;

		if (setState(state)) {

			event = IssueEventBO.newChangeState(to.getId(), state);

			if (event != null) {
				isAdded = to.getEvents().add(event);

				if (isAdded) {
					to.setLastModifiedDate(event.getDate());
					changedIssueTO = to;
				} else {
					isError = true;
				}
			} else {
				isError = true;
			}
		}

		if (isError) {
			changedIssueTO = null;
			setState(originalState);
		}

		return changedIssueTO;
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
