package com.rest.tutorial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.rest.tutorial.pojo.TvSeries;

public interface TvSeriesDao {
	
	@Select("select * from tv_series where status=0")
	public List<TvSeries> getAll();
	
	@Select("select * from tv_series where id=#{id}")
	public TvSeries getOneById(int id);
	
	public int insert(TvSeries tvSeries);
	
	public int update(TvSeries tvSeries);
	
	@Delete("delete from tv_series where id=#{id}")
	public int delete(int id);
	
	@Delete("update tv_series set status=-1, delete_reason=#{reason} where id=#{id}")
	public int logicDelete(int id, String reason);
}
