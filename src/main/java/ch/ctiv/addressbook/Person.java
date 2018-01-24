package ch.ctiv.addressbook;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nicolas Regez
 * @since 23 Jan 2018
 */
@Entity
@Table(name = "person")
public class Person {

	@Id
	@Column(name = "id", nullable = false, updatable = false, length = 40)
	private String id;

	@Column(name = "firstname", length = 255)
	private String firstname;

	@Column(name = "lastname", length = 255)
	private String lastname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}
