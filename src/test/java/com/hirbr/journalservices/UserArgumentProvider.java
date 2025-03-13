//package com.hirbr.journalservices;
//
//import java.util.stream.Stream;
//
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//
//import com.hirbr.journalservices.entity.User;
//
//public class UserArgumentProvider implements ArgumentsProvider{
//
//	@Override
//	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
//		return Stream.of(
//				Arguments.of(new User("priya",))
//				);
//	}
//
//}
