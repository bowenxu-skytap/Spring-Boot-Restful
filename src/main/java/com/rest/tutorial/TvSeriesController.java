package com.rest.tutorial;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.IOUtils;
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
	public TvSeriesDto insertOne(@Valid @RequestBody TvSeriesDto tvSeriesDto) {
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
	
	@DeleteMapping("/{id}")
	public Map<String, String> deleteOne(@PathVariable int id, HttpServletRequest request,
			@RequestParam(value = "delete_reason", required = false) String deleteReason) {
		if (log.isTraceEnabled()) {
			log.trace("deleteOne " + id);
		}
		Map<String, String> res = new HashMap<>();
		if (id == 101) {
			res.put("message", "#101 is deleted by " + request.getRemoteAddr() + 
					" and(reason: " + deleteReason + ")");
		} else if (id == 102) {
			throw new RuntimeException("#120 cannot be deleted");
		} else {
			throw new ResourceNotFoundException();
		}
		return res;
	}
	
	@PostMapping(value="/{id}/photos", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void addPhoto(@PathVariable int id, @RequestParam("photo") MultipartFile imgFile) throws Exception {
		if (log.isTraceEnabled()) {
			log.trace(id + " has received the file with file: " + imgFile.getOriginalFilename());
		}
		FileOutputStream fos = new FileOutputStream("target/" + imgFile.getOriginalFilename());
		IOUtils.copy(imgFile.getInputStream(), fos);
		fos.close();
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