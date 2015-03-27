package es.ua.dlsi.mejorua.api.transfer;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author https://github.com/ElMesa
 * 
 * This class represents the target to solve of the @see es.ua.dlsi.mejorua.api.transfer.IssueTO
 * 
 * It can represent 3 types of targets
 *     1. A generic target
 *         * Uses type = Type.GENERIC
 *         * Holds the descrition in IssueTargetTO.genericDescription
 *     2. A University of Alicante element (http://datos.ua.es/es/ficha-datos.html?idDataset=805)
 *         * Uses type = Type.ELEMENT
 *         * Uses typeId and id to the element type and id
 *         * Unused IssueTargetTO.genericDescription in this scenario
 *     3. A University of Alicante characteristic (http://datos.ua.es/es/ficha-datos.html?idDataset=804)
 *         * Uses type = Type.CHARACTERISTIC
 *         * Uses typeId and id to the characteristic type and id
 *         * Unused IssueTargetTO.genericDescription in this scenario
 */
@Entity
public class IssueTargetTO {

	public enum Type {GENERIC, ELEMENT, CHARACTERISTIC};
	
	/**
	 * Internal id for persistance
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long _id;
	
	/**
	 * See class description for details
	 */
	@Basic
	Type type;
	
	/**
	 * See class description for details
	 */
	@Basic
	String typeId;
	
	/**
	 * See class description for details
	 */
	@Basic
	String id;
	
	/**
	 * See class description for details
	 */
	@Basic
	String genericDescription;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGenericDescription() {
		return genericDescription;
	}

	public void setGenericDescription(String genericDescription) {
		this.genericDescription = genericDescription;
	}

}
