package ca.softwareadd.api

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import java.util.*
import javax.validation.ConstraintViolationException

/**
 * @author Eugene Ossipov
 */
@RestControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ErrorInfo> = internalHandler(ex, INTERNAL_SERVER_ERROR)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException(ex: Exception): ResponseEntity<ErrorInfo> = internalHandler(ex, NOT_FOUND)

    @ExceptionHandler(WebExchangeBindException::class, ConstraintViolationException::class)
    fun validationException(ex: Exception): ResponseEntity<ErrorInfo> = internalHandler(ex, BAD_REQUEST)

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationException(ex: Exception): ResponseEntity<ErrorInfo> = internalHandler(ex, CONFLICT)

    private fun internalHandler(ex: Exception, status: HttpStatus): ResponseEntity<ErrorInfo> =
            ResponseEntity.status(status)
                    .body(
                            ErrorInfo(
                                    status.reasonPhrase,
                                    ex.javaClass.name,
                                    ex.localizedMessage ?: ex.message,
                                    status.value()
                            )
                    )
}
