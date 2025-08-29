package service.Delivery.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "de8pw5oem", // Replace with your Cloudinary cloud name
                "api_key", "183394635361418",       // Replace with your Cloudinary API key
                "api_secret", "K3qpYCZiALpRWaOxZ_D1Po5PM5A"  // Replace with your Cloudinary API secret
        ));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "agent-documents",
                        "resource_type", "auto"
                ));
        return uploadResult.get("secure_url").toString();
    }
}