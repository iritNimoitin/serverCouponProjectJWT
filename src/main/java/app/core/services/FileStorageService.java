//package app.core.services;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import app.core.entities.Coupon.Category;
//import app.core.exceptions.CouponSystemException;
//
//@Service
//public class FileStorageService {
//	
//	@Value("${file.upload-dir}")
//	private String storageDir;
//	private Path fileStoragePath;
//
//	@PostConstruct
//	public void init() throws CouponSystemException {
//
//		this.fileStoragePath = Paths.get(this.storageDir).toAbsolutePath();
//
//		try {
//			// create the directory
//			Files.createDirectories(fileStoragePath);
//		} catch (IOException e) {
//			throw new CouponSystemException("could not create directory", e);
//		}
//		for (Category category : Category.values()) { 
//			String categoryDir = storageDir + "\\" + category.toString();
//			Path categoryPath = Paths.get(categoryDir).toAbsolutePath();
//
//			try {
//				// create the directory
//				Files.createDirectories(categoryPath);
//			} catch (IOException e) {
//				throw new CouponSystemException("could not create directory", e);
//			}
//		}
//
//	}
//	
//	public FileStorageService() {
//	}
//	
//	public void storeFile(MultipartFile file, Category category, int id) throws CouponSystemException {
//		String fileName = file.getOriginalFilename();
//		if (fileName.contains("..")) {
//			throw new CouponSystemException("file name contains ilegal caharacters");
//		}
//		// copy the file to the destination directory (if already exists replace)
//		try {
//			Path targetLocation = Paths.get(this.fileStoragePath.toString(), category.toString(), String.valueOf(id)).toAbsolutePath();
//			Files.createDirectories(targetLocation);
//			targetLocation.resolve(fileName);
//			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
////			return "pics/" + fileName;
//		} catch (IOException e) {
//			throw new CouponSystemException("storing file " + fileName + " failed", e);
//		}
//	}
//}
