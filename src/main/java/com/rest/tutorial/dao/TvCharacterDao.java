package com.rest.tutorial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.rest.tutorial.pojo.TvCharacter;

public interface TvCharacterDao {

	@Select("select * from tv_character where tv_series_id=#{tvSeriesId}")
	public List<TvCharacter> getAllByTvSeriesId(int tvSeriesId);
	
	@Select("select * from tv_character where id=#{id}")
	public TvCharacter getOneById(int id);
	
	public int insert(TvCharacter tvCharacter);
	
	public int update(TvCharacter tvCharacter);
	
	@Delete("delete from tv_character where id=#{id}")
	public int delete(int id);
}
