package com.example.canteen.controller;


import com.example.canteen.dto.dtos.ImageDto;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.entity.Image;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(ApiResponse.builder().message("Upload ảnh thành công!").data(imageDtos).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder().message(ErrorCode.FAIL_TO_UPLOAD_IMAGE.getMessage() + e.getMessage()).build());
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downLoadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }
    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestBody MultipartFile file){
        Image image = imageService.getImageById(imageId);
        try {
            if(image != null){
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(ApiResponse.builder().message("Cập nhật ảnh thành công!").build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder().message(ErrorCode.FAIL_TO_UPLOAD_IMAGE.getMessage()).build());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder().message("Cập nhật ảnh thất bại!").build());
    }
    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        Image image = imageService.getImageById(imageId);
        try {
            if(image != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(ApiResponse.builder().message("Xóa ảnh thành công!").build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder().message(ErrorCode.FAIL_TO_UPLOAD_IMAGE.getMessage()).build());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder().message("Xóa ảnh thất bại!").build());
    }

}
