package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

public class ArgsTest {

    Method method;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        method = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    @Test
    void args() {
        Assertions.assertThat(pointcut("args(String)").matches(method, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(Object)").matches(method, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args()").matches(method, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(pointcut("args(..)").matches(method, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(*)").matches(method, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(String, ..)").matches(method, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsVsExecution() {

        //Args
        Assertions.assertThat(pointcut("args(String)").matches(method, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(java.io.Serializable)").matches(method, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(Object)").matches(method, MemberServiceImpl.class)).isTrue();

        //Execution
        Assertions.assertThat(pointcut("execution (* *(String))").matches(method, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("execution (* *(java.io.Serializable))").matches(method, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(pointcut("execution (* *(Object))").matches(method, MemberServiceImpl.class)).isFalse();
    }
}
