package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "applicationactions", schema = "cnf")
public class ApplicationActions extends BaseEntity {
	@Id
	@Column(name = "actionid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int actionId;
	@Column(name = "actioncode")
	private String actionCode;
	@Column(name = "actiondescription")
	private String actionDescription;
	@Column(name = "statusid")
	private int statusId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ApplicationActions that)) return false;
		if (!super.equals(o)) return false;
		return actionId == that.actionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), actionId);
	}
}
