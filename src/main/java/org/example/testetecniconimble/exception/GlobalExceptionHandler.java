package org.example.testetecniconimble.exception;


import org.example.testetecniconimble.entity.Conta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AutorizadorExternoException.class)
    public ResponseEntity<ErrorResposta> handleAutorizadorExternoException(AutorizadorExternoException ex) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return new ResponseEntity<>(new ErrorResposta(status.value(),
                "Erro de Serviço Externo",
                ex.getMessage()), status);
    }

    @ExceptionHandler(CancelamentoException.class)
    public ResponseEntity<ErrorResposta> handleCancelamentoException(CancelamentoException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(new ErrorResposta(status.value(),
                "Erro durante o cancenlamento",
                ex.getMessage()), status);
    }

    @ExceptionHandler(DepositoException.class)
    public ResponseEntity<ErrorResposta> handleDepositoException(DepositoException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(new ErrorResposta(status.value(),
                "Erro durante no Deposito",
                ex.getMessage()), status);
    }

    @ExceptionHandler(CobrancaExceptional.class)
    public ResponseEntity<ErrorResposta> handleCobrancaException(CobrancaExceptional ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(new ErrorResposta(status.value(),
                "Erro de Cobranca",
                ex.getMessage()), status);
    }

    @ExceptionHandler(ContaNaoEncontradaExceptional.class)
    public ResponseEntity<ErrorResposta> handleContaNaoEncontradaException(ContaNaoEncontradaExceptional ex) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(new ErrorResposta(status.value(),
                "Erro conta não encontrada",
                ex.getMessage()), status);
    }

}
