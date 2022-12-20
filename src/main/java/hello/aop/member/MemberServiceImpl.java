package hello.aop.member;

import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ClassAop
@Service
public class MemberServiceImpl implements MemberService{
    @Override
    @MethodAop("test value")
    public String hello(String param) {
        log.info("MemberService");
        return "ok";
    }

    public String internal(String param) {
        return "ok";
    }
}
