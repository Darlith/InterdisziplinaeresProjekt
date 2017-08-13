package com.eufh.drohne.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRONTEST")
public class Person {

	@Id
	@Column(name = "PERSONALNUMMER")
	private String personalnummer;
	@Column(name = "ARBEITSANTEIL")
	private String arbeitsanteil;
	@Column(name = "DATUM")
	private Date datum;

	public Date getDatum() {
		return datum;
	}

	public String getPersonalnummer() {
		return personalnummer;
	}

	public void setPersonalnummer(String personalnummer) {
		this.personalnummer = personalnummer;
	}

	public String getArbeitsanteil() {
		return arbeitsanteil;
	}

	public void setArbeitsanteil(String arbeitsanteil) {
		this.arbeitsanteil = arbeitsanteil;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}
}
