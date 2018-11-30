package com.rest.tutorial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		list.add(createPoi());
		list.add(createWestWorld());
		return list;
	}
	
	@GetMapping("/{id}")
	public TvSeriesDto getOne(@PathVariable int id) {
		if (log.isTraceEnabled()) {
			log.trace("getOne " + id);
		}
		if (id == 101) {
			return createWestWorld();
		} else if (id == 102) {
			return createPoi();
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@PostMapping
	public TvSeriesDto insertOne(@RequestBody TvSeriesDto tvSeriesDto) {
		if (log.isTraceEnabled()) {
			log.trace("Passing object: " + tvSeriesDto);
		}
		//test
		tvSeriesDto.setId(9999);
		return tvSeriesDto;
	}
	
	@PutMapping("/{id}")
	public TvSeriesDto updateOne(@PathVariable int id, @RequestBody TvSeriesDto tvSeriesDto) {
		if (log.isTraceEnabled()) {
			log.trace("updateOne " + id);
		}
		if (id == 101 || id == 102) {
			return createPoi();
		}
		throw new ResourceNotFoundException();
	}
	
    /**
     * 创建电视剧“Person of Interest",仅仅方便此节做展示其他方法用，以后章节把数据存储到数据库后，会删除此方法
     */
    private TvSeriesDto createPoi() {
        Calendar c = Calendar.getInstance();
        c.set(2011, Calendar.SEPTEMBER, 22, 0, 0, 0);
        return new TvSeriesDto(102, "Person of Interest", 5, c.getTime());
    }
    /**
     * 创建电视剧“West World",仅仅方便此节做展示其他方法用，以后章节把数据存储到数据库后，会删除此方法
     */
    private TvSeriesDto createWestWorld() {
        Calendar c = Calendar.getInstance();
        c.set(2016, Calendar.OCTOBER, 2, 0, 0, 0);
        return new TvSeriesDto(101, "West World", 1, c.getTime());
    }
}