package es.ua.dlsi.mejorua.api.persistance;

import java.util.List;

import es.ua.dlsi.mejorua.api.transfer.IssueTO;

public interface IIssueDAO {

	// ////////////////////////////////////////////////////////////////////////////////////
	//
	// IssueTO - Collection
	//
	// ////////////////////////////////////////////////////////////////////////////////////
	
	// Read
	public List<IssueTO> getAll();

	// Create - NOT ALLOWED - No bulk CRUD "modifiers"
	// Update - NOT ALLOWED - No bulk CRUD "modifiers"
	// Delete - NOT ALLOWED - No bulk CRUD "modifiers"

	// ////////////////////////////////////////////////////////////////////////////////////
	//
	// IssueTO - Individual resource
	//
	// ////////////////////////////////////////////////////////////////////////////////////
	
	// Create
	public long create(IssueTO issue);
	
	// Read
	public IssueTO get(long id);
	
	// Update
	public boolean update(IssueTO issue);
	
	// Delete - NOT ALLOWED - Nowadays issues only change state, there is no delete functionality
}
