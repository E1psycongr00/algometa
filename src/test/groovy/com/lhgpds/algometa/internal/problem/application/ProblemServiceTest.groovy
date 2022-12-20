package com.lhgpds.algometa.internal.problem.application

import com.lhgpds.algometa.exception.common.problem.ProblemNotFoundException
import com.lhgpds.algometa.internal.member.application.dto.MemberDto
import com.lhgpds.algometa.internal.member.domain.vo.Email
import com.lhgpds.algometa.internal.member.domain.vo.Nickname
import com.lhgpds.algometa.internal.problem.application.dto.ProblemDto
import com.lhgpds.algometa.internal.problem.domain.vo.Platform
import com.lhgpds.algometa.internal.problem.domain.vo.ProblemId
import com.lhgpds.algometa.internal.problem.domain.vo.code.*
import com.lhgpds.algometa.internal.problem.domain.vo.content.Content
import com.lhgpds.algometa.internal.problem.repository.HistoryRepository
import com.lhgpds.algometa.internal.problem.repository.ProblemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.security.access.AccessDeniedException
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

    def memberDtoBuilding() {
        long id = 1L
        String email = "helloworld@naver.com"
        String nickname = "back"
        return MemberDto.builder()
                .id(id)
                .email(Email.from(email))
                .nickname(Nickname.from(nickname))
                .build()
    }

    def makeProblemDto() {
        Content content = Content.of("타이틀", "http://backjoon", "쉽다.")
        Code code = Code.of("print(123)", Language.PYTHON, "00:10:23", Status.SUCCESS, Difficulty.EASY)
        return ProblemDto.builder()
                .problemId(ProblemId.nextProblemId())
                .content(content)
                .code(code)
                .category(new LinkedHashSet<Category>(List.of(Category.ARRAY, Category.STACK)))
                .platform(Platform.BACKJOON)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()
    }

    def checkReturnDto(ProblemDto result) {
        assert result.getCode() != null
        assert result.getCategory() != null
        assert result.getPlatform() != null
        assert result.getCreatedAt() != null
        assert result.getModifiedAt() != null
    }

    def "문제 등록 영속성 및 동작 테스트"() {
        given:
        MemberDto userDto = memberDtoBuilding()
        ProblemDto problemDto = makeProblemDto()

        when:
        ProblemDto result = problemService.addProblem(userDto, problemDto)

        then:
        print("result: " + result)
        checkReturnDto(result)
    }

    def "문제 컨텐츠 수정 영속성 및 동작 테스트"() {
        given:
        MemberDto userDto = memberDtoBuilding()
        ProblemDto problemDto = makeProblemDto()
        Content afterContent = Content.of("title", "link", "main")
        problemService.addProblem(userDto, problemDto)

        when:
        ProblemDto result = problemService.updateProblemContent(1L, problemDto.getProblemId(), afterContent)

        then:
        checkReturnDto(result)
    }

    def "문제 컨텐츠 수정 id가 존재하지 않는 경우 예외 테스트"() {
        given:
        MemberDto userDto = memberDtoBuilding()
        ProblemDto problemDto = makeProblemDto()
        Content afterContent = Content.of("title", "link", "main")
        problemService.addProblem(userDto, problemDto)

        when:
        problemService.updateProblemContent(2L, ProblemId.nextProblemId(), afterContent)

        then:
        thrown(ProblemNotFoundException.class)
    }

    def "문제 컨텐츠 수정 id 주인이 아닌 사용자가 요청시 권한 예외 테스트"() {
        given:
        MemberDto userDto = memberDtoBuilding()
        ProblemDto problemDto = makeProblemDto()
        Content afterContent = Content.of("title", "link", "main")
        problemService.addProblem(userDto, problemDto)

        when:
        problemService.updateProblemContent(2L, problemDto.getProblemId(), afterContent)

        then:
        thrown(AccessDeniedException.class)
    }
}
