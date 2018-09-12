package io.knowledgeassets.myskills.server.aspect;

import io.knowledgeassets.myskills.server.exception.MethodArgumentNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * Aspect for checking binding result. Also, I've used an extra annotation (@CheckBindingResult) which should be
 * put above the rest methods. This annotation makes the aspect flexible, so that we can have a method that has
 * BindingResult and we can handle it and the aspect will not be triggered for that.
 * <p>
 * I deliberately wrote several Pointcuts, so that I could use them in other advices if necessary.
 *
 * @author hadi
 */
@Slf4j
@Aspect
@Component
public class BindingResultAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) && " +
            "@annotation(io.knowledgeassets.myskills.server.aspect.CheckBindingResult)")
    public void methodAcceptBindingResult() {
    }

    @Pointcut("execution(* io.knowledgeassets.myskills.server..*Controller.*(..))")
    public void controllers() {
    }

    @Pointcut("execution(* *(.., org.springframework.validation.BindingResult, ..))")
    public void getBindingResult() {
    }

    @Before("methodAcceptBindingResult() && controllers() && getBindingResult()")
    public void myAdvice(JoinPoint jp) throws MethodArgumentNotValidException {
        for (Object arg : jp.getArgs()) {
            if (!(arg instanceof BindingResult))
                continue;
            BindingResult bindingResult = (BindingResult) arg;
            if (bindingResult.hasErrors()) {
                log.error("Input data is corrupted.");
                throw MethodArgumentNotValidException.builder()
                        .code(10003L)
                        .bindingResult(bindingResult)
                        .build();
            }
        }
    }

}
