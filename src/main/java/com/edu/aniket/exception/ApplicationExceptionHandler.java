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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.edu.aniket.config.ResponcseStructure;

@RestController
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(UserWithEmailAndPasswordNotFound.class)
	public ResponseEntity<ResponcseStructure<String>> handleUserEmailAndPasswordNotFound( UserWithEmailAndPasswordNotFound e) 
	{
		ResponcseStructure<String> responcseStructure = new ResponcseStructure<>();
		responcseStructure.setData(e.getMessage());
		responcseStructure.setMessage("User Info Not Valid");
		responcseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<> (responcseStructure , HttpStatus.NOT_FOUND);
	}
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid( MethodArgumentNotValidException exception, HttpHeaders headers,HttpStatusCode status, WebRequest request) 
	{
		List<ObjectError> errors = exception.getAllErrors();
		Map<String, String> errorMessages = new HashMap<>();
		for (ObjectError objectError : errors) 
		{
			String filedName = ((FieldError) objectError).getField();
			String errorMessage = objectError.getDefaultMessage();
			errorMessages.put(filedName, errorMessage);
		}
		ResponcseStructure<Map<String, String>> responseStructure = new ResponcseStructure<>();
		responseStructure.setData(errorMessages);
		responseStructure.setMessage("Verify The User Info");
		responseStructure.setStatus(HttpStatus.BAD_REQUEST.value());
		
		return new ResponseEntity<Object>(responseStructure, HttpStatus.BAD_REQUEST);		
	}
	
	@ExceptionHandler(UserIsNotValiedToAddItem.class)
	public ResponseEntity<ResponcseStructure<String>> handleUserIsNotValiedToAddItem(
			UserIsNotValiedToAddItem exception) {
		ResponcseStructure<String> responseStructure = new ResponcseStructure<>();
		responseStructure.setData(exception.getMessage());
		responseStructure.setMessage("User Is Not Valid to Add Item");
		responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
	}
}
