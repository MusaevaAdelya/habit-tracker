package com.example.habittracker.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long tokenId;
	private String confirmationToken;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@OneToOne(targetEntity = Userr.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "id")
	private Userr user;
	
	public ConfirmationToken(Userr user) {
		this.user = user;
	}

	public ConfirmationToken(Userr user, String generatedString) {
		this.user = user;
		this.confirmationToken = generatedString;
	}
	
}
