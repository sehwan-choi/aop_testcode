package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExamRepository {

    private static int seq =0;

    @Trace
    public String save(String itemId) {
      log.info("[save] seq = " + seq);
        seq++;
        if (seq % 5 ==0) {
//        if (seq >= 5) {
            throw new IllegalStateException("예외 발생");
        }

        return "ok";
    }
}
