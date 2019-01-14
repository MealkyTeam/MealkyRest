package com.mealky.rest.controller;

import java.util.Map;

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

    @PostMapping("/sec/image")
    public ResponseEntity<Object> testImage(@RequestParam("file") MultipartFile file) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "NAME",
                "api_key", "KEY",
                "api_secret", "SECRET"));
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("transformation",
                    new Transformation().width(1280).height(720).crop("fit")));
            return new ResponseEntity<Object>(new MessageWrapper((String) uploadResult.get("url")), HttpStatus.OK);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return new ResponseEntity<Object>(new MessageWrapper(ApiError.SOMETHING_WENT_WRONG.error()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
