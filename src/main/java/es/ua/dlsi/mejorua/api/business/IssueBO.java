package es.ua.dlsi.mejorua.api.business;

import java.util.ArrayList;
import java.util.HashMap;

import es.ua.dlsi.mejorua.api.persistance.IssueDAO;

public class IssueBO {

	private long id;
	private float latitude;
	private float longitude;
	private String action;
	private String term;

	public static HashMap<Long, IssueBO> getAll() {
		return IssueDAO.getAll();
	}
	
	public static IssueBO get(long id) {
		return IssueDAO.get(id);
	}

	public static long add(IssueBO issue) {
		
		long newId = -1;
		
		if (issue.isValid()) {
			newId = IssueDAO.add(issue);
		}
		
		return newId;
	}

	public boolean update() {

		boolean isSaved = false;

		if (isValid() && get(id) != null) {
			IssueDAO.update(this);
			isSaved = true;
		}

		return isSaved;
	}

	// ///////////////////////
	//
	// Validators
	//
	// ///////////////////////

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

		if (id > 0)
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

	// ///////////////////////
	//
	// GETTERS AND SETTERS
	//
	// ///////////////////////

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

}
