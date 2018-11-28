package com.rest.tutorial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tvseries")
public class TvSeriesController {

	@GetMapping
	public List<TvSeriesDto> getAll() {
		List<TvSeriesDto> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, 10, 2, 0, 0);
		list.add(new TvSeriesDto(1, "WestWorld", 1, calendar.getTime()));
		return list;
	}
}