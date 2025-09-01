package com.hirbr.journalservices.util;

import java.time.Duration;

//com.hirbr.journalservices.web.CookieUtil
import org.springframework.http.ResponseCookie;

public final class CookieUtil {
	private CookieUtil() {
	}

	public static ResponseCookie refreshCookie(String token, long maxAgeSeconds) {
		return ResponseCookie.from("refreshToken", token).httpOnly(true).secure(false) // true in HTTPS
				.sameSite("Lax").path("/").maxAge(maxAgeSeconds).build();
	}

	public static ResponseCookie clearRefreshCookie() {
		return ResponseCookie.from("refreshToken", "").httpOnly(true).secure(false).sameSite("Lax").path("/").maxAge(Duration.ZERO)
				.build();
	}
}
