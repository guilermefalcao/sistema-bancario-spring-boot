package com.exemplo.conta.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import jakarta.persistence.EntityNotFoundException;

/**
 * Tratador Global de Erros para JWT Authentication
 * 
 * Esta classe centraliza o tratamento de todas as exceções da aplicação,
 * fornecendo respostas HTTP padronizadas e mensagens de erro consistentes.
 * 
 * @author Guilherme - Dataprev
 */
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DadosErroResposta> tratarErro404(EntityNotFoundException ex) {
        var erro = new DadosErroResposta(
            "RECURSO_NAO_ENCONTRADO",
            "O recurso solicitado não foi encontrado",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DadosErroValidacao> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        var dadosErro = new DadosErroValidacao(
            "DADOS_INVALIDOS",
            "Os dados enviados são inválidos",
            erros.stream().map(DadosErroValidacaoCampo::new).toList()
        );
        return ResponseEntity.badRequest().body(dadosErro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DadosErroResposta> tratarErro400Json(HttpMessageNotReadableException ex) {
        var erro = new DadosErroResposta(
            "JSON_INVALIDO",
            "O formato JSON enviado é inválido",
            "Verifique a sintaxe do JSON e tente novamente"
        );
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DadosErroResposta> tratarErroBadCredentials(BadCredentialsException ex) {
        var erro = new DadosErroResposta(
            "CREDENCIAIS_INVALIDAS",
            "Login ou senha incorretos",
            "Verifique suas credenciais e tente novamente"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DadosErroResposta> tratarErroAuthentication(AuthenticationException ex) {
        var erro = new DadosErroResposta(
            "FALHA_AUTENTICACAO",
            "Falha na autenticação",
            "Token inválido, expirado ou ausente. Faça login novamente"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosErroResposta> tratarErroAcessoNegado(AccessDeniedException ex) {
        var erro = new DadosErroResposta(
            "ACESSO_NEGADO",
            "Acesso negado ao recurso",
            "Você não tem permissão para acessar este recurso"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DadosErroResposta> tratarErroNegocio(RuntimeException ex) {
        String mensagem = ex.getMessage();
        
        if (mensagem.contains("Saldo insuficiente")) {
            var erro = new DadosErroResposta(
                "SALDO_INSUFICIENTE",
                "Saldo insuficiente para a operação",
                mensagem
            );
            return ResponseEntity.badRequest().body(erro);
        }
        
        if (mensagem.contains("CPF já cadastrado")) {
            var erro = new DadosErroResposta(
                "CPF_DUPLICADO",
                "CPF já está cadastrado no sistema",
                mensagem
            );
            return ResponseEntity.badRequest().body(erro);
        }
        
        var erro = new DadosErroResposta(
            "ERRO_NEGOCIO",
            "Erro de regra de negócio",
            mensagem
        );
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosErroResposta> tratarErro500(Exception ex) {
        var erro = new DadosErroResposta(
            "ERRO_INTERNO",
            "Erro interno do servidor",
            "Ocorreu um erro inesperado. Tente novamente mais tarde"
        );
        
        System.err.println("❌ Erro interno: " + ex.getMessage());
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    public record DadosErroResposta(
        String codigo,
        String titulo,
        String detalhe
    ) {}

    public record DadosErroValidacao(
        String codigo,
        String titulo,
        java.util.List<DadosErroValidacaoCampo> erros
    ) {}

    public record DadosErroValidacaoCampo(
        String campo,
        String mensagem
    ) {
        public DadosErroValidacaoCampo(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}