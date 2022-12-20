package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method method;

    @BeforeEach
    public void init() throws Exception {
        method = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        // execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)
        // ?는 생략할 수 있다.
        //* 같은 패턴을 지정할 수 있다
        // execution(public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String))
        log.info("method={}", method);
    }

    @Test
    void exactMatch() {
        // method=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactFalse() {
        //hello.aop 패키지만 매칭하기 때문에 MemberServiceImpl의 메서드는 pointcut에 해당X
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactSubPackage1() {
        //hello.aop 하위 패키지 모두 포함이기 때문에 MemberServiceImpl의 메서드는 pointcut에 해당된다
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactSubPackage2() {
        //hello.aop 하위 패키지 모두 포함이기 때문에 MemberServiceImpl의 메서드는 pointcut에 해당된다
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    //String 타입의 파라미터 허용
    //(String)
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatch2() {
        pointcut.setExpression("execution(* *())");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void argsMatchStar3() {
        pointcut.setExpression("execution(* *(*))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        Assertions.assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }
}
