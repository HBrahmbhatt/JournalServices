//package com.hirbr.journalservices;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import org.junit.jupiter.api.Test; // junit after version 5 are called jupiter
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.hirbr.journalservices.entity.User;
//import com.hirbr.journalservices.repository.UserRepository;
//import com.hirbr.journalservices.services.UserService;
//
//@SpringBootTest
//class JournalservicesApplicationTests {
//
//	@Autowired
//	UserRepository userRepository;
//	
//	@Autowired
//	UserService userService;
//	
//	@Test
//	void contextLoads() {
//	}
//	
//	// You can start testing for your methods with junit
//	@ParameterizedTest
//	@ArgumentsSource(UserArgumentProvider.class)
//	public void testFindUserByUsername(User user) {
//		assertTrue(userService.saveUser(user));
//	}
//	
//}
