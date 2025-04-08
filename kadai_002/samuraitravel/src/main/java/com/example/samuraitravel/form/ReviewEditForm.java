package com.example.samuraitravel.form;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEditForm {
	@NotNull
	private Integer id;
	
	private Integer houseId;
	
	//private Integer userId;
	
	private Integer evaluation;
	
	private String comment;


	


}