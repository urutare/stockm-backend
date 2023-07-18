package com.urutare.stockmcategory.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryUtil {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile image) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }

    public String deleteImage(String imageUrl) throws IOException {
        String publicId = extractPublicIdFromImageUrl(imageUrl);
        Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return (String) deleteResult.get("result");
    }

    public String imageName(String name) {
        String newName;
        if (name.split(" ").length > 1) {
            newName = String.join("_", name.split(" "));
            System.out.println(newName);
            return newName;
        }
        newName = String.join("", name.split(" "));
        return newName;

    }

    public String extractPublicIdFromImageUrl(String imageUrl) {
        int startIndex = imageUrl.lastIndexOf("/") + 1;
        int endIndex = imageUrl.lastIndexOf(".");
        return imageUrl.substring(startIndex, endIndex);
    }
}