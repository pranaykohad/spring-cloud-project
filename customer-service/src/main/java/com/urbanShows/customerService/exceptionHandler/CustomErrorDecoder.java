package com.urbanShows.customerService.exceptionHandler;

import java.io.IOException;

import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

//@Slf4j
//public class CustomErrorDecoder implements ErrorDecoder {
//
//	@Override
//	public Exception decode(String methodKey, Response response) {
//		switch (response.status()) {
//		case 403:
//			try {
//				String string = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8);
//				return new AuthrizationDeniedException(string);
//			} catch (IOException e) {
//				log.info("Error while parsing 403: {} ", e.getLocalizedMessage());
//			}
//		case 409:
//			try {
//				String string = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8);
//				return new UserAlreadyExistsException(string);
//			} catch (IOException e) {
//				log.info("Error while parsing 409: {} ", e.getLocalizedMessage());
//			}
//		case 401:
//			try {
//				String string = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8);
//				return new UnAuthorizedException(string);
//			} catch (IOException e) {
//				log.info("Error while parsing 401: {} ", e.getLocalizedMessage());
//			}
//		default:
//			return new Exception("Generic error: " + response.request().url());
//		}
//	}
//
//}
