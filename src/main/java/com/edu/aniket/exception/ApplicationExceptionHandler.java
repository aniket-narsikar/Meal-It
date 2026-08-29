package com.edu.aniket.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.edu.aniket.config.ResponseStructure;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UserWithEmailAndPasswordNotFound.class)
	public ResponseEntity<ResponseStructure<String>> handleUserEmailAndPasswordNotFound(UserWithEmailAndPasswordNotFound e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("User Credentials Invalid");
		responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserIdNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleUserIdNotFoundException(UserIdNotFoundException e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("User Id Not Found");
		responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ItemWithIdNotFound.class)
	public ResponseEntity<ResponseStructure<String>> handleItemWithIdNotFound(ItemWithIdNotFound e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("Item Not Found");
		responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FoodProductWithTheGivenIdNotFound.class)
	public ResponseEntity<ResponseStructure<String>> handleFoodProductNotFound(FoodProductWithTheGivenIdNotFound e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("Food Product Not Found");
		responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FoodMenuNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleFoodMenuNotFound(FoodMenuNotFoundException e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("Food Menu Not Found");
		responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FoodOrderNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleFoodOrderNotFound(FoodOrderNotFoundException e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("Food Order Not Found");
		responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserIsNotValidToAddItemException.class)
	public ResponseEntity<ResponseStructure<String>> handleUserIsNotValidToAddItemException(UserIsNotValidToAddItemException exception) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(exception.getMessage());
		responseStructure.setMessage("User Access Denied");
		responseStructure.setStatus(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({org.springframework.security.authentication.BadCredentialsException.class, org.springframework.security.core.AuthenticationException.class})
	public ResponseEntity<ResponseStructure<String>> handleAuthenticationException(Exception e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("Unauthorized: Authentication Failed");
		responseStructure.setStatus(HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ResponseEntity<ResponseStructure<String>> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("Forbidden: Access Denied");
		responseStructure.setStatus(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseStructure<String>> handleIllegalArgumentException(IllegalArgumentException e) {
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData(e.getMessage());
		responseStructure.setMessage("Bad Request");
		responseStructure.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> errors = exception.getAllErrors();
		Map<String, String> errorMessages = new HashMap<>();
		for (ObjectError objectError : errors) {
			String fieldName = ((FieldError) objectError).getField();
			String errorMessage = objectError.getDefaultMessage();
			errorMessages.put(fieldName, errorMessage);
		}
		ResponseStructure<Map<String, String>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(errorMessages);
		responseStructure.setMessage("Validation Error");
		responseStructure.setStatus(HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(responseStructure, HttpStatus.BAD_REQUEST);
	}
}
