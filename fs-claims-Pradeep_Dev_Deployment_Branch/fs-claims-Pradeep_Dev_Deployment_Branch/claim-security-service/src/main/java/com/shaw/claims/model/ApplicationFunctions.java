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
@Table(name = "applicationfunctions", schema = "cnf")
public class ApplicationFunctions extends BaseEntity {

	@Id
	@Column(name = "functionid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int functionId;

	@Column(name = "functioncode")
	private String functionCode;

	@Column(name = "functiondescription")
	private String functionDescription;

	@Column(name = "statusid")
	private int statusId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ApplicationFunctions that)) return false;
		if (!super.equals(o)) return false;
		return functionId == that.functionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), functionId);
	}
}
