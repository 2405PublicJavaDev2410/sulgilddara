package com.makjan.sulgilddara.brewery.model.vo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BreweryTag {
	private Integer breweryTagId;
	private String breweryTagName;
	private Integer breweryId;
}
