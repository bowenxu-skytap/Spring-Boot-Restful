package com.rest.tutorial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tvseries")
public class TvSeriesController {
	private static final Log log = LogFactory.getLog(TvSeriesController.class);

	@GetMapping
	public List<TvSeriesDto> getAll() {
		if (log.isTraceEnabled()) {
			log.trace("getAll() is called");
		}
		List<TvSeriesDto> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, Calendar.OCTOBER, 2, 0, 0);
		list.add(new TvSeriesDto(1, "WestWorld", 1, calendar.getTime()));
		calendar.set(2011, Calendar.SEPTEMBER, 22, 0, 0);
		list.add(new TvSeriesDto(2, "Person of Interest", 5, calendar.getTime()));
		return list;
	}
}