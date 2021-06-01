package app.core.services;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.repositories.CouponRepository;

@Service
@Transactional
public class SharedService {
	
	private CouponRepository couponRepository;
	
	@Autowired
	public SharedService(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}
	
	public Page<Coupon> getCoupons(int pageNumber, int itemsPerPage, String sortBy) {
		Pageable page = PageRequest.of(pageNumber, itemsPerPage, Sort.by(sortBy));
		Page<Coupon> coupons = couponRepository.findAll(page);
		return new PageImpl<Coupon>(setCouponImages(coupons.getContent()), page, coupons.getTotalElements());
	}
	
	public Page<Coupon> getCouponsByCategory(Category category, int pageNumber, int itemsPerPage, String sortBy) {
		Pageable page = PageRequest.of((pageNumber)*itemsPerPage, itemsPerPage, Sort.by(sortBy));
		Page<Coupon> coupons = couponRepository.findByCategory(category, page);
		return new PageImpl<Coupon>(setCouponImages(coupons.getContent()), page, coupons.getTotalElements());
	}
	
    public List<Coupon> setCouponImages(List<Coupon> coupons) {
    	for(Coupon coupon : coupons) {
    		String directoryPath =  "C:\\Users\\lidor\\eclipse-workspace\\serverCouponProjectJWT\\src\\main\\resources\\static\\pics\\"
    									+ coupon.getCategory() + "\\" + coupon.getId();
    		File directory = new File(directoryPath);
    		File[] images =  directory.listFiles();
    		String[] imagesNames;
    		String[] encodedImages;
    		try {
    			imagesNames = new String[images.length];
    			encodedImages = new String[images.length];
    		} catch (Exception e) {
    			continue;
    		}
    		for(int i = 0; i < images.length; i++) {
    			imagesNames[i] = images[i].getName();
    			byte[] fileContent = null;
    			try {
    				fileContent = FileUtils.readFileToByteArray(images[i]);
    			} catch (IOException e) {
    				throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    			}
    			String encodedString = Base64.getEncoder().encodeToString(fileContent);
    			encodedImages[i] = "data:image/jpeg;base64," + encodedString;
    		}
    		coupon.setImages(encodedImages, imagesNames);
    	}
	    return coupons;
    }
}
