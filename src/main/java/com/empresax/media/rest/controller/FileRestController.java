package com.empresax.media.rest.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.empresax.media.rest.util.FileResponse;
import com.empresax.media.service.IFileService;


@RestController
@RequestMapping(value = "/v1/files")
public class FileRestController {

    @Autowired
    private IFileService fileService;

    @Value(value = "${project.image}")
    private String path;

    @PostMapping(value = "/imgs")
    public ResponseEntity<FileResponse> fileUploadImg(@RequestParam MultipartFile image) {
        String fileName = null;
        try {
            fileName = fileService.uploadImg(path, image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new FileResponse(fileName, "Ha ocurrido un error al momento de subir la img"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                new FileResponse(fileName, "Imagen subida correctamente"),
                HttpStatus.OK);
    }

    @GetMapping(value = "/imgs/{img}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImg(@PathVariable String img, HttpServletResponse response) {
        try {
            InputStream resource = fileService.getImg(path, img);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
