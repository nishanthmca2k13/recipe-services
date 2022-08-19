package nl.abnamro.recipes.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//Custom Error Response Class for Recipes Web Service
@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
	private final int status;
	private final String message;
	private final LocalDateTime dateTime;
}
