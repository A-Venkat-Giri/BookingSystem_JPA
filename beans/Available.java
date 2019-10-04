package com.dev.bss.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name="availability")
public class Available {
	@Id @Column(name="avail_id") @GeneratedValue(strategy=GenerationType.AUTO)
	private int availId;
	@Column(name = "bus_id")
	private int busId;
	private int availSeats;
	@Temporal(value = TemporalType.DATE)
	private Date journeyDate;
	

   	public int getAvailId() {
		return availId;
	}
	public void setAvailId(int availId) {
		this.availId = availId;
	}
	public int getBusId() {
		return busId;
	}
	public void setBusId(int busId) {
		this.busId = busId;
	}
	public int getAvailSeats() {
		return availSeats;
	}
	public void setAvailSeats(int availSeats) {
		this.availSeats = availSeats;
	}
	public Date getJourneyDate() {
		return journeyDate;
	}
	public void setJourneyDate(Date journeyDate) {
		this.journeyDate = journeyDate;
	}
	@Override
	public String toString() {
		return "Available [availId=" + availId + ", busId=" + busId + ", availSeats=" + availSeats + ", journeyDate="
				+ journeyDate + "]";
	}
	
	
	

}
