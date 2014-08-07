package es.ua.dlsi.mejorua.api.persistance;

import java.util.HashMap;

import es.ua.dlsi.mejorua.api.business.IssueBO;

public interface IIssueDAO {

	// ////////////////////////////////////////////////////////////////////////////////////
	//
	// IssueBO - Collection
	//
	// ////////////////////////////////////////////////////////////////////////////////////
	
	// Read
	public HashMap<Long, IssueBO> getAll();

	// Create - NOT ALLOWED - No bulk CRUD "modifiers"
	// Update - NOT ALLOWED - No bulk CRUD "modifiers"
	// Delete - NOT ALLOWED - No bulk CRUD "modifiers"

	// ////////////////////////////////////////////////////////////////////////////////////
	//
	// IssueBO - Individual resource
	//
	// ////////////////////////////////////////////////////////////////////////////////////
	
	// Create
	public long create(IssueBO issue);
	
	// Read
	public IssueBO get(long id);
	
	// Update
	public boolean update(IssueBO issue);
	
	// Delete - NOT ALLOWED - Nowadays issues only change state, there is no delete functionality
}
