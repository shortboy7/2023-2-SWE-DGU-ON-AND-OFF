package backend.spectrum.dguonoff.security.error.exception;

import backend.spectrum.dguonoff.global.statusCode.ErrorCode;
import io.jsonwebtoken.JwtException;

public class JwtTokenException extends JwtException{
    private final ErrorCode errorCode;

    public JwtTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
