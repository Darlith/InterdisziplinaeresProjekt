package com.eufh.drohne.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICES")
public class Drohne {

	@Id
	@Column(name = "ID")
	private int id;
	@Column(name = "LIEFERORT")
	private String lieferort;
	@Column(name = "GEWICHT")
	private String gewicht;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLieferort() {
		return lieferort;
	}
	public void setLieferort(String lieferort) {
		this.lieferort = lieferort;
	}
	public String getGewicht() {
		return gewicht;
	}
	public void setGewicht(String gewicht) {
		this.gewicht = gewicht;
	}

}
