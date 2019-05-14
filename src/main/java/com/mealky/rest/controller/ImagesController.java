package com.mealky.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.mealky.rest.model.ApiError;
import com.mealky.rest.model.wrapper.MessageWrapper;

@RestController
public class ImagesController {
	
	@Autowired
	Cloudinary cloudinary;
	@PostMapping("/sec/image")
	public ResponseEntity<Object> uploadImage(@RequestParam("file") MultipartFile[] file)
	{
  	Map uploadResult = null;
  	List<String> urlList = new ArrayList<String>();
  	Map<String,List<String>> map = new HashMap<>();
    try {
    	for(MultipartFile  f : file) {
        uploadResult = cloudinary.uploader().upload(f.getBytes(), ObjectUtils.asMap("transformation",
                new Transformation().width(1280).height(720).crop("fit")));
        urlList.add((String)uploadResult.get("secure_url"));
    	}
    	map.put("images", urlList);
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    } catch (Exception e1) {
        e1.printStackTrace();
    }
    return new ResponseEntity<Object>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()), HttpStatus.INTERNAL_SERVER_ERROR);
}
}
