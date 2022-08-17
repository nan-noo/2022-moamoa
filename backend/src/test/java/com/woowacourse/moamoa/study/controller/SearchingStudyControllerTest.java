package com.woowacourse.moamoa.study.controller;

import static com.woowacourse.moamoa.fixtures.StudyFixtures.HTTP_스터디_신청서;
import static com.woowacourse.moamoa.fixtures.StudyFixtures.리액트_스터디_신청서;
import static com.woowacourse.moamoa.fixtures.StudyFixtures.알고리즘_스터디_신청서;
import static com.woowacourse.moamoa.fixtures.StudyFixtures.자바_스터디_신청서;
import static com.woowacourse.moamoa.fixtures.StudyFixtures.자바스크립트_스터디_신청서;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.woowacourse.moamoa.common.RepositoryTest;
import com.woowacourse.moamoa.common.utils.DateTimeSystem;
import com.woowacourse.moamoa.member.domain.Member;
import com.woowacourse.moamoa.member.domain.repository.MemberRepository;
import com.woowacourse.moamoa.member.query.MemberDao;
import com.woowacourse.moamoa.member.query.data.MemberData;
import com.woowacourse.moamoa.study.domain.repository.StudyRepository;
import com.woowacourse.moamoa.study.query.StudyDetailsDao;
import com.woowacourse.moamoa.study.query.StudySummaryDao;
import com.woowacourse.moamoa.study.query.data.StudyDetailsData;
import com.woowacourse.moamoa.study.service.SearchingStudyService;
import com.woowacourse.moamoa.study.service.StudyService;
import com.woowacourse.moamoa.study.service.request.StudyRequest;
import com.woowacourse.moamoa.study.service.response.StudiesResponse;
import com.woowacourse.moamoa.study.service.response.StudyDetailResponse;
import com.woowacourse.moamoa.tag.query.TagDao;
import com.woowacourse.moamoa.tag.query.response.TagData;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RepositoryTest
public class SearchingStudyControllerTest {

    private SearchingStudyController sut;

    @Autowired
    private StudySummaryDao studySummaryDao;

    @Autowired
    private StudyDetailsDao studyDetailsDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private EntityManager entityManager;

    private Long javaStudyId;
    private Long reactStudyId;
    private Long javaScriptId;
    private Long httpStudyId;
    private Long algorithmStudyId;
    private Long linuxStudyId;
    private Member jjanggu;
    private Member greenlawn;
    private Member dwoo;
    private Member verus;

    @BeforeEach
    void initDataBase() {
        jjanggu = memberRepository.save(new Member(1L, "jjanggu", "https://image", "github.com"));
        greenlawn = memberRepository.save(new Member(2L, "greenlawn", "https://image", "github.com"));
        dwoo = memberRepository.save(new Member(3L, "dwoo", "https://image", "github.com"));
        verus = memberRepository.save(new Member(4L, "verus", "https://image", "github.com"));

        StudyService studyService = new StudyService(studyRepository, memberRepository, new DateTimeSystem());

        StudyRequest javaStudyRequest = 자바_스터디_신청서(List.of(1L, 2L, 3L), 10, LocalDate.now());
        javaStudyId = studyService.createStudy(jjanggu.getGithubId(), javaStudyRequest).getId();

        StudyRequest reactStudyRequest = 리액트_스터디_신청서(List.of(2L, 4L, 5L), 5, LocalDate.now());
        reactStudyId = studyService.createStudy(dwoo.getGithubId(), reactStudyRequest).getId();

        StudyRequest javaScriptStudyRequest = 자바스크립트_스터디_신청서(List.of(2L, 4L), LocalDate.now());
        javaScriptId = studyService.createStudy(jjanggu.getGithubId(), javaScriptStudyRequest).getId();

        StudyRequest httpStudyRequest = HTTP_스터디_신청서(List.of(2L, 3L), LocalDate.now());
        httpStudyId = studyService.createStudy(jjanggu.getGithubId(), httpStudyRequest).getId();

        StudyRequest algorithmStudyRequest = 알고리즘_스터디_신청서(List.of(), LocalDate.now());
        algorithmStudyId = studyService.createStudy(jjanggu.getGithubId(), algorithmStudyRequest).getId();

        StudyRequest linuxStudyRequest = StudyRequest.builder()
                .title("Linux 스터디").excerpt("리눅스 설명").thumbnail("linux thumbnail").description("Linux를 공부하자의 베루스입니다.")
                .startDate(LocalDate.now()).endDate(LocalDate.now()).enrollmentEndDate(LocalDate.now())
                .tagIds(List.of())
                .build();
        linuxStudyId = studyService.createStudy(verus.getGithubId(), linuxStudyRequest).getId();

        studyService.participateStudy(dwoo.getGithubId(), javaStudyId);
        studyService.participateStudy(verus.getGithubId(), javaStudyId);

        studyService.participateStudy(jjanggu.getGithubId(), reactStudyId);
        studyService.participateStudy(greenlawn.getGithubId(), reactStudyId);
        studyService.participateStudy(verus.getGithubId(), reactStudyId);

        studyService.participateStudy(dwoo.getGithubId(), javaScriptId);
        studyService.participateStudy(verus.getGithubId(), javaScriptId);

        entityManager.flush();
        entityManager.clear();
    }

    @BeforeEach
    void setUp() {
        sut = new SearchingStudyController(
                new SearchingStudyService(studySummaryDao, studyDetailsDao, memberDao, tagDao));
    }

    @DisplayName("페이징 정보로 스터디 목록 조회")
    @Test
    public void getStudies() {
        ResponseEntity<StudiesResponse> response = sut.getStudies(PageRequest.of(0, 3));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isHasNext()).isTrue();
        assertThat(response.getBody().getStudies())
                .hasSize(3)
                .extracting("id", "title", "excerpt", "thumbnail", "recruitmentStatus")
                .containsExactlyElementsOf(List.of(
                        tuple(linuxStudyId, "Linux 스터디", "리눅스 설명", "linux thumbnail", "RECRUITMENT_START"),
                        tuple(algorithmStudyId, "알고리즘 스터디", "알고리즘 설명", "algorithm thumbnail", "RECRUITMENT_START"),
                        tuple(httpStudyId, "HTTP 스터디", "HTTP 설명", "http thumbnail", "RECRUITMENT_START"))
                );
    }

    @DisplayName("빈 문자열로 검색시 전체 스터디 목록에서 조회")
    @Test
    void searchByBlankKeyword() {
        ResponseEntity<StudiesResponse> response = sut
                .searchStudies("", emptyList(), emptyList(), emptyList(),
                        PageRequest.of(0, 3)
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isHasNext()).isTrue();
        assertThat(response.getBody().getStudies())
                .hasSize(3)
                .extracting("id", "title", "excerpt", "thumbnail", "recruitmentStatus")
                .containsExactlyElementsOf(List.of(
                        tuple(linuxStudyId, "Linux 스터디", "리눅스 설명", "linux thumbnail", "RECRUITMENT_START"),
                        tuple(algorithmStudyId, "알고리즘 스터디", "알고리즘 설명", "algorithm thumbnail", "RECRUITMENT_START"),
                        tuple(httpStudyId, "HTTP 스터디", "HTTP 설명", "http thumbnail", "RECRUITMENT_START"))
                );
    }

    @DisplayName("문자열로 검색시 해당되는 스터디 목록에서 조회")
    @Test
    void searchByKeyword() {
        ResponseEntity<StudiesResponse> response = sut
                .searchStudies("Java 스터디", emptyList(), emptyList(), emptyList(),
                        PageRequest.of(0, 3)
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isHasNext()).isFalse();
        assertThat(response.getBody().getStudies())
                .hasSize(1)
                .extracting("id", "title", "excerpt", "thumbnail", "recruitmentStatus")
                .contains(tuple(javaStudyId, "Java 스터디", "자바 설명", "java thumbnail", "RECRUITMENT_START"));
    }

    @DisplayName("앞뒤 공백을 제거한 문자열로 스터디 목록 조회")
    @Test
    void searchWithTrimKeyword() {
        ResponseEntity<StudiesResponse> response = sut
                .searchStudies("   Java 스터디   ", emptyList(), emptyList(), emptyList(), PageRequest.of(0, 3));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isHasNext()).isFalse();
        assertThat(response.getBody().getStudies())
                .hasSize(1)
                .extracting("id", "title", "excerpt", "thumbnail", "recruitmentStatus")
                .contains(tuple(javaStudyId, "Java 스터디", "자바 설명", "java thumbnail", "RECRUITMENT_START"));
    }

    @DisplayName("다른 종류의 필터들은 AND 조건으로 스터디 목록을 조회")
    @Test
    void searchByDifferentKindFilters() {
        List<Long> tags = List.of(5L); // React
        List<Long> areas = List.of(3L); // BE

        ResponseEntity<StudiesResponse> response = sut
                .searchStudies("", emptyList(), areas, tags, PageRequest.of(0, 3));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isHasNext()).isFalse();
        assertThat(response.getBody().getStudies()).hasSize(0);
    }

    @DisplayName("같은 종류의 필터들은 OR 조건으로 스터디 목록을 조회")
    @Test
    void searchBySameAndDifferentKindFilters() {
        List<Long> generationIds = List.of(2L); // 4기
        List<Long> areaIds = List.of(3L, 4L); // BE, FE
        List<Long> tagIds = List.of(1L, 5L); // Java, React
        ResponseEntity<StudiesResponse> response = sut
                .searchStudies("", generationIds, areaIds, tagIds, PageRequest.of(0, 3));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isHasNext()).isFalse();
        assertThat(response.getBody().getStudies())
                .hasSize(2)
                .extracting("id", "title", "excerpt", "thumbnail", "recruitmentStatus")
                .contains(
                        tuple(javaStudyId, "Java 스터디", "자바 설명", "java thumbnail", "RECRUITMENT_START"),
                        tuple(reactStudyId, "React 스터디", "리액트 설명", "react thumbnail", "RECRUITMENT_START")
                );
    }

    @DisplayName("스터디 상세 정보를 조회할 수 있다.")
    @Test
    public void getStudyDetails() {
        StudyDetailsData expect = StudyDetailsData.builder()
                // Study Content
                .id(javaStudyId).title("Java 스터디").excerpt("자바 설명").thumbnail("java thumbnail")
                .status("RECRUITMENT_START").description("그린론의 우당탕탕 자바 스터디입니다.").createdDate(LocalDate.now())
                // Study Participant
                .currentMemberCount(3).maxMemberCount(10)
                .owner(new MemberData(jjanggu.getGithubId(), "jjanggu", "https://image", "github.com"))
                // Study Period
                .startDate(LocalDate.now())
                .build();

        final List<Tuple> expectParticipants = List.of(
                tuple(dwoo.getGithubId(), "dwoo", "https://image", "github.com"),
                tuple(verus.getGithubId(), "verus", "https://image", "github.com")
        );

        final List<Tuple> expectAttachedTags = List.of(
                tuple("Java", "자바"),
                tuple("4기", "우테코4기"),
                tuple("BE", "백엔드")
        );

        final ResponseEntity<StudyDetailResponse> response = sut.getStudyDetails(javaStudyId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertStudyContent(response.getBody(), expect);
        assertStudyParticipants(response.getBody(), expect, expectParticipants);
        assertStudyPeriod(response.getBody(), expect);
        assertAttachedTags(response.getBody().getTags(), expectAttachedTags);
    }

    @DisplayName("선택적으로 입력 가능한 정보를 포함한 스터디 상세 정보를 조회할 수 있다.")
    @Test
    public void getStudyDetailsWithOptional() {
        final StudyDetailsData expect = StudyDetailsData.builder()
                // Study Content
                .id(reactStudyId).title("React 스터디").excerpt("리액트 설명").thumbnail("react thumbnail")
                .status("RECRUITMENT_START").description("디우의 뤼액트 스터디입니다.").createdDate(LocalDate.now())
                // Study Participant
                .currentMemberCount(4).maxMemberCount(5)
                .owner(new MemberData(dwoo.getGithubId(), "dwoo", "https://image", "github.com"))
                // Study Period
                .enrollmentEndDate(LocalDate.now())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        final List<Tuple> expectParticipants = List.of(
                tuple(jjanggu.getGithubId(), "jjanggu", "https://image", "github.com"),
                tuple(greenlawn.getGithubId(), "greenlawn", "https://image", "github.com"),
                tuple(verus.getGithubId(), "verus", "https://image", "github.com")
        );

        final List<Tuple> expectAttachedTags = List.of(
                tuple("4기", "우테코4기"),
                tuple("FE", "프론트엔드"),
                tuple("React", "리액트")
        );

        final ResponseEntity<StudyDetailResponse> response = sut.getStudyDetails(reactStudyId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertStudyContent(response.getBody(), expect);
        assertStudyParticipants(response.getBody(), expect, expectParticipants);
        assertStudyPeriod(response.getBody(), expect);
        assertAttachedTags(response.getBody().getTags(), expectAttachedTags);
    }

    @DisplayName("스터디 참여자와 부착된 태그가 없는 스터디의 세부사항 조회")
    @Test
    void getNotHasParticipantsAndAttachedTagsStudyDetails() {
        final StudyDetailsData expect = StudyDetailsData.builder()
                // Study Content
                .id(linuxStudyId).title("Linux 스터디").excerpt("리눅스 설명").thumbnail("linux thumbnail")
                .status("RECRUITMENT_START").description("Linux를 공부하자의 베루스입니다.").createdDate(LocalDate.now())
                // Study Participant
                .currentMemberCount(1)
                .owner(new MemberData(verus.getGithubId(), "verus", "https://image", "github.com"))
                // Study Period
                .startDate(LocalDate.now())
                .enrollmentEndDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        final List<Tuple> expectParticipants = List.of();
        final List<Tuple> expectAttachedTags = List.of();

        final ResponseEntity<StudyDetailResponse> response = sut.getStudyDetails(linuxStudyId);
        final StudyDetailResponse actual = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isNotNull();
        assertStudyContent(actual, expect);
        assertStudyParticipants(actual, expect, expectParticipants);
        assertStudyPeriod(actual, expect);
        assertAttachedTags(actual.getTags(), expectAttachedTags);
    }

    @DisplayName("스터디 디테일 정보 조회 시 스터디원들이 가입한 스터디의 수와 가입날짜도 함께 조회한다.")
    @Test
    public void findStudyDetailsWithNumberOfStudy() {
        final ResponseEntity<StudyDetailResponse> response = sut.getStudyDetails(javaStudyId);

        final StudyDetailResponse responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody.getMembers())
                .filteredOn(member -> member.getParticipationDate() != null)
                .hasSize(2)
                .extracting("githubId", "username", "imageUrl", "profileUrl", "numberOfStudy")
                .containsExactlyInAnyOrder(
                        tuple(dwoo.getGithubId(), dwoo.getUsername(), dwoo.getImageUrl(), dwoo.getProfileUrl(), 3),
                        tuple(verus.getGithubId(), verus.getUsername(), verus.getImageUrl(), verus.getProfileUrl(), 4)
                );
    }

    private void assertStudyContent(final StudyDetailResponse actual, final StudyDetailsData expect) {
        assertThat(actual.getId()).isEqualTo(expect.getId());
        assertThat(actual.getTitle()).isEqualTo(expect.getTitle());
        assertThat(actual.getExcerpt()).isEqualTo(expect.getExcerpt());
        assertThat(actual.getThumbnail()).isEqualTo(expect.getThumbnail());
        assertThat(actual.getRecruitmentStatus()).isEqualTo(expect.getRecruitmentStatus());
        assertThat(actual.getDescription()).isEqualTo(expect.getDescription());
        assertThat(actual.getCreatedDate()).isEqualTo(expect.getCreatedDate().toString());
    }

    private void assertStudyParticipants(
            final StudyDetailResponse actual, final StudyDetailsData expect, List<Tuple> expectParticipants
    ) {
        assertThat(actual.getCurrentMemberCount()).isEqualTo(expect.getCurrentMemberCount());
        assertThat(actual.getMaxMemberCount()).isEqualTo(expect.getMaxMemberCount());
        assertThat(actual.getOwner()).isEqualTo(expect.getOwner());
        assertThat(actual.getMembers())
                .hasSize(expectParticipants.size())
                .extracting("githubId", "username", "imageUrl", "profileUrl")
                .containsExactlyInAnyOrderElementsOf(expectParticipants);
    }

    private void assertStudyPeriod(final StudyDetailResponse actual, final StudyDetailsData expect) {
        assertThat(actual.getEnrollmentEndDate()).isEqualTo(expect.getEnrollmentEndDate());
        assertThat(actual.getEndDate()).isEqualTo(expect.getEndDate());
        assertThat(actual.getStartDate()).isEqualTo(expect.getStartDate().toString());
    }

    private void assertAttachedTags(List<TagData> actual, List<Tuple> expect) {
        assertThat(actual)
                .hasSize(expect.size())
                .filteredOn(tag -> tag.getId() != null)
                .extracting("name", "description")
                .containsExactlyInAnyOrderElementsOf(expect);
    }
}
