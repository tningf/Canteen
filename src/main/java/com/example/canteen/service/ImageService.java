package com.example.canteen.service;


import com.example.canteen.dto.dtos.ImageDto;
import com.example.canteen.entity.Image;
import com.example.canteen.entity.Product;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductService productService;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));
    }

    public void deleteImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        // Delete from S3
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(image.getFileName())
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to delete file from S3: " + e.getMessage());
        }

        imageRepository.delete(image);
    }

    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // Generate unique filename
                String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                // Upload to S3
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(uniqueFileName)
                        .contentType(file.getContentType())
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build();

                s3Client.putObject(putObjectRequest,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                // Generate S3 URL
                String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                        bucketName, region, uniqueFileName);

                // Save image metadata to database
                Image image = new Image();
                image.setFileName(uniqueFileName);
                image.setFileType(file.getContentType());
                image.setDownloadUrl(s3Url);  // Set the S3 URL directly as download URL
                image.setProduct(product);

                Image savedImage = imageRepository.save(image);

                // Create DTO
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());  // Use S3 URL
                savedImageDto.add(imageDto);

            } catch (IOException e) {
                throw new RuntimeException("Failed to read file: " + e.getMessage());
            } catch (S3Exception e) {
                throw new RuntimeException("Failed to upload file to S3: " + e.getMessage());
            }
        }
        return savedImageDto;
    }

    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            // Delete old file from S3
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(image.getFileName())
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            // Upload new file
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Generate new S3 URL
            String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, region, uniqueFileName);

            // Update image metadata
            image.setFileName(uniqueFileName);
            image.setFileType(file.getContentType());
            image.setDownloadUrl(s3Url);  // Set the S3 URL directly as download URL
            imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to update file in S3: " + e.getMessage());
        }
    }

    public Image getImageByProductId(Long productId) {
        return imageRepository.findByProductId(productId).stream()
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));
    }
}