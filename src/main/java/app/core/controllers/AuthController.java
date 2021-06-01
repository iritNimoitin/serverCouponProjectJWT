package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.jwt.JwtUtil;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.ClientType;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import app.core.services.LoginManager;

@RestController
@CrossOrigin
@RequestMapping("/Auth")
public class AuthController {
	
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private AdminService service;
	@Autowired
	private JwtUtil jwt;

	@PostMapping("/login")
	public String login(@RequestHeader String email, @RequestHeader String password, @RequestHeader String clientType) {
		ClientService service;
		String token = "";
		ClientType userType = ClientType.valueOf(clientType);
		System.out.println(email);
		try {
			service = loginManager.login(email, password, userType);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		
		switch(userType) {
		case Customer:
			token = jwt.generateToken(((CustomerService) service).getCustomerDetails(), userType);
			break;
		case Company:
			token = jwt.generateToken(((CompanyService) service).getCompanyDetails(), userType);
			break;
		case Administrator:
			token = jwt.generateToken(null, userType);
			break;
		}
		return token;
	}
	
	@PostMapping("/register/company")
	public Object registerCompany(@RequestHeader String email, @RequestHeader String password, @RequestHeader String name) {
		Company company = new Company(name, email, password);
		try {
			service.addCompany(company);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return login(email, password, ClientType.Company.toString());
	}
	
	@PostMapping("/register/customer")
	public Object registerCustomer(@RequestHeader String email, @RequestHeader String password, @RequestHeader String firstName, @RequestHeader String lastName) {
		Customer customer = new Customer(firstName, lastName, email, password);
		try {
			service.addCustomer(customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return login(email, password, ClientType.Customer.toString());
	}
	
}
