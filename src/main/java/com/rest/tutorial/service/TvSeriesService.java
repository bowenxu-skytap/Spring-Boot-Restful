package com.rest.tutorial.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rest.tutorial.dao.TvCharacterDao;
import com.rest.tutorial.dao.TvSeriesDao;
import com.rest.tutorial.pojo.TvCharacter;
import com.rest.tutorial.pojo.TvSeries;

@Service
public class TvSeriesService {
	private static final Log log = LogFactory.getLog(TvSeriesService.class);
	
	@Autowired private TvSeriesDao seriesDao;
	@Autowired private TvCharacterDao characterDao;

	@Transactional(readOnly=true)
	public List<TvSeries> getAllTvSeries() {
        try {
            Thread.sleep(10);
        }catch(Exception e) {
            
        }
        if(log.isTraceEnabled()) {
            log.trace("getAllTvSeries started   ");
        }
        List<TvSeries> res = seriesDao.getAll();
        for (TvSeries ts: res) {
        	ts.setTvCharacters(characterDao.getAllByTvSeriesId(ts.getId()));
        }
		return res;
	}
	
	@Transactional(readOnly=true)
	public TvSeries getTvSeriesById(int tvSeriesId) {
        if(log.isTraceEnabled()) {
            log.trace("getTvSeriesById started for " + tvSeriesId);
        }
        TvSeries series = seriesDao.getOneById(tvSeriesId);
        if (series != null) {
        	series.setTvCharacters(characterDao.getAllByTvSeriesId(tvSeriesId));
        }
        return series;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public TvSeries addTvSeries(TvSeries tvSeries) {
        if(log.isTraceEnabled()) {
            log.trace("addTvSeries started for " + tvSeries);
        }
        seriesDao.insert(tvSeries);
        if (tvSeries.getId() == null) {
        	throw new RuntimeException("cannot get primary key id");
        }
        for (TvCharacter tc: tvSeries.getTvCharacters()) {
        	tc.setTvSeriesId(tvSeries.getId());
        	characterDao.insert(tc);
        }
        return tvSeries;
	}
	
	public TvSeries updateTvSeries(TvSeries tvSeries) {
        if(log.isTraceEnabled()) {
            log.trace("updateTvSeries started for " + tvSeries);
        }
        seriesDao.update(tvSeries);
        return tvSeries;
	}
	
	public TvCharacter addTvCharacter(TvCharacter tvCharacter) {
		characterDao.insert(tvCharacter);
		return tvCharacter;
	}
	
    public TvCharacter updateTvCharacter(TvCharacter tvCharacter) {
        characterDao.update(tvCharacter);
        return tvCharacter;
    }
    
    public boolean deleteTvSeries(int id) {
        if(log.isTraceEnabled()) {
            log.trace("deleteTvSeries started for " + id);
        }
        seriesDao.delete(id);
        return true;
    }
    
    public boolean deleteTvSeries(int id, String reason) {
        if(log.isTraceEnabled()) {
            log.trace("deleteTvSeries started for " + id);
        }
        seriesDao.logicDelete(id, reason);
        return true;
    }
    
}
