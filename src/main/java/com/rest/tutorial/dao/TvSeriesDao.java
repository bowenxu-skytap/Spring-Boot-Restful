package com.rest.tutorial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.rest.tutorial.pojo.TvSeries;

public interface TvSeriesDao {
	
	@Select("select * from tv_series where status=0")
	public List<TvSeries> getAll();
}
