package com.lhgpds.algometa.internal.problem.service

import com.lhgpds.algometa.internal.member.domain.vo.Email
import com.lhgpds.algometa.internal.member.domain.vo.Nickname
import com.lhgpds.algometa.internal.member.service.dto.MemberDto
import com.lhgpds.algometa.internal.problem.domain.vo.Platform
import com.lhgpds.algometa.internal.problem.domain.vo.code.*
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content
import com.lhgpds.algometa.internal.problem.repository.HistoryRepository
import com.lhgpds.algometa.internal.problem.repository.ProblemRepository
import com.lhgpds.algometa.internal.problem.service.dto.ProblemDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ProblemService.class)
class ProblemServiceTest extends Specification {

    @Autowired
    ProblemRepository problemRepository

    @Autowired
    HistoryRepository historyRepository

    @Autowired
    ProblemService problemService

    def memberDtoBuilding(Long id, String email, String nickname) {
        return MemberDto.builder()
                .id(id)
                .email(Email.from(email))
                .nickname(Nickname.from(nickname))
                .build()
    }


    def "테이블 영속성 및 동작 테스트"() {
        given:
        MemberDto userDto = memberDtoBuilding(1L, "helloworld@naver.com", "back")
        Content content = Content.of("타이틀", "http://backjoon", "쉽다.")
        Code code = Code.of("print(123)", Language.PYTHON, "00:10:23", Status.SUCCESS, Difficulty.EASY)
        ProblemDto problemDto = ProblemDto.builder()
                .content(content)
                .code(code)
                .category(new LinkedHashSet<Category>(List.of(Category.ARRAY, Category.STACK)))
                .platform(Platform.BACKJOON)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()

        when:
        ProblemDto result = problemService.addProblem(userDto, problemDto)

        then:
        print("result: " + result)

        assert result.getCode() != null
        assert result.getCategory() != null
        assert result.getPlatform() != null
        assert result.getCreatedAt() != null
        assert result.getModifiedAt() != null
    }
}
