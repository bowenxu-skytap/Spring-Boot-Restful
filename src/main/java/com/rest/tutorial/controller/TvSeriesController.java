package com.rest.tutorial.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rest.tutorial.pojo.TvSeries;
import com.rest.tutorial.service.TvSeriesService;

@RestController
@RequestMapping("/tvseries")
public class TvSeriesController {
	private static final Log log = LogFactory.getLog(TvSeriesController.class);
	
	@Autowired TvSeriesService tvSeriesService;
	@Value("${tutorial.uploadFolder:target/upload-files}") 
	String uploadFolder;

	@GetMapping
	public List<TvSeries> getAll() {
		if (log.isTraceEnabled()) {
			log.trace("getAll() is called");
		}
		List<TvSeries> list = tvSeriesService.getAllTvSeries();
		if (log.isTraceEnabled()) {
			log.trace("total get " + list.size() + " records");
		}
		return list;
	}
	
	@GetMapping("/{id}")
	public TvSeries getOne(@PathVariable int id) {
		if (log.isTraceEnabled()) {
			log.trace("getOne " + id);
		}
		TvSeries ts = tvSeriesService.getTvSeriesById(id);
		if (ts == null) {
			throw new ResourceNotFoundException();
		}
		return ts;
	}
	
	@PostMapping
	public TvSeries insertOne(@Valid @RequestBody TvSeries tvSeries) {
		if (log.isTraceEnabled()) {
			log.trace("Passing object: " + tvSeries);
		}
		tvSeriesService.addTvSeries(tvSeries);
		return tvSeries;
	}
	
	@PutMapping("/{id}")
	public TvSeries updateOne(@PathVariable int id, @RequestBody TvSeries tvSeries) {
		if (log.isTraceEnabled()) {
			log.trace("updateOne " + id);
		}
		TvSeries ts = tvSeriesService.getTvSeriesById(id);
		if (ts == null) {
			throw new ResourceNotFoundException();
		}
		ts.setSeasonCount(tvSeries.getSeasonCount());
		ts.setName(tvSeries.getName());
		ts.setOriginRelease(tvSeries.getOriginRelease());
		tvSeriesService.updateTvSeries(ts);
		return ts;
	}
	
	@DeleteMapping("/{id}")
	public Map<String, String> deleteOne(@PathVariable int id, HttpServletRequest request,
			@RequestParam(value = "delete_reason", required = false) String deleteReason) {
		if (log.isTraceEnabled()) {
			log.trace("deleteOne " + id);
		}
		Map<String, String> res = new HashMap<>();
		TvSeries ts = tvSeriesService.getTvSeriesById(id);	
		if (ts == null) {
			throw new ResourceNotFoundException();
		} else {
			tvSeriesService.deleteTvSeries(id, deleteReason);
			res.put("message", "#" + id + " is deleted by " + request.getRemoteAddr() + 
					" and(reason: " + deleteReason + ")");
		}
		return res;
	}
	
	@PostMapping(value="/{id}/photos", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<String, String> addPhoto(@PathVariable int id, 
			@RequestParam("photo") MultipartFile imgFile) throws Exception {
		if (log.isTraceEnabled()) {
			log.trace("TV series " + id + " has received the file with file: " + imgFile.getOriginalFilename());
		}
		//Save file
		File folder = new File(uploadFolder);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		String fileName = imgFile.getOriginalFilename();
		if(fileName.endsWith(".jpg")) {
			FileOutputStream fos = new FileOutputStream(new File(folder, fileName));
			IOUtils.copy(imgFile.getInputStream(), fos);
			fos.close();
			Map<String, String> res = new HashMap<>();
			res.put("photo", fileName);
			return res;
		} else {
			throw new RuntimeException("Unsupported format，only support jpg format");
		}
	}
	
    @GetMapping(value="/{id}/icon", produces=MediaType.IMAGE_JPEG_VALUE)
    public byte[] getIcon(@PathVariable int id) throws Exception{
        if(log.isTraceEnabled()) {
            log.trace("getIcon(" + id + ")");
        }
        String iconFilePath = "src/test/resources/icon.jpg";
        InputStream is = new FileInputStream(iconFilePath);
        return IOUtils.toByteArray(is);
    }
	
    /**
     * 创建电视剧“Person of Interest",仅仅方便此节做展示其他方法用，以后章节把数据存储到数据库后，会删除此方法
     */
//    private TvSeries createPoi() {
//        Calendar c = Calendar.getInstance();
//        c.set(2011, Calendar.SEPTEMBER, 22, 0, 0, 0);
//        return new TvSeries(102, "Person of Interest", 5, c.getTime());
//    }
    /**
     * 创建电视剧“West World",仅仅方便此节做展示其他方法用，以后章节把数据存储到数据库后，会删除此方法
     */
//    private TvSeries createWestWorld() {
//        Calendar c = Calendar.getInstance();
//        c.set(2016, Calendar.OCTOBER, 2, 0, 0, 0);
//        return new TvSeries(101, "West World", 1, c.getTime());
//    }
}