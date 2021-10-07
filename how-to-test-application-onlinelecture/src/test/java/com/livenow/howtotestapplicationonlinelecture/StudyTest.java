package com.livenow.howtotestapplicationonlinelecture;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("before all - 테스트 이전 한번만 실행");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after all - 테스트 이후 한번만 실행");
    }

    @BeforeEach
    void setUp() {
        System.out.println("before each - 테스트 이전 매번 실행");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each - 테스트 이후 매번 실행");
    }

    @Test
    @DisplayName("스터디 만들기")
    void create_new_study() {
        //given
        final Study study = new Study(10);
        //when
        //then
        assertNotNull(study);
        assertThat(study).isNotNull();
    }

    @Test
    @Disabled
    void create_new_study_again() {
        //given
        final Study study = new Study(10);
        //when
        //then
        assertNotNull(study);
        assertThat(study).isNotNull();
    }

    @Test
    void create_test() {
        //given
        final Study study = new Study(10);
        //when
        //then
        /**
         * assertAll을 하면 여러개의 테스트가 깨짐을 확인할 수 있다. (하나씩 적으면 맨 위에서 터지면 다음 에러는 확인하기 힘듬)
         */
        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT,
                        study.getStatus(),
                        () -> "스터디를 처음 만들면 상태값이" + StudyStatus.DRAFT + "여야 한다"
                ), // 연산을 람다식으로 만들어주면 최대한 필요한 시점에 함. 즉, 실패 할때만 실행하게 할 수 있다. (그냥 하면 모든 순간에 연산을 실행함)
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야한다")

        );
        assertAll(
                () -> assertThat(study).isNotNull(),
                () -> assertThat(study.getStatus()).as(() -> "스터디를 처음 만들면 상태값이" + StudyStatus.DRAFT + "여야 한다")
                        .isEqualTo(StudyStatus.DRAFT),
                () -> assertThat(study.getLimit() > 0).as("스터디 최대 참석 가능 인원은 0보다 커야한다")
                        .isTrue()
        );
    }

    @Test
    void create_test_with_unchecked_exception() {
        //given
        //when
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        //then
        assertEquals("limit는 0보다 커야한다.", exception.getMessage());
        assertThatThrownBy(() -> new Study(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("limit는 0보다 커야한다.");

    }

    @Test
    void create_test_with_time_check() {
        //given
        //when
        //then
        /**
         * 100ms가 넘으면 바로 끊고 싶다면 assertTimeoutPreemptively 사용
         */
        assertTimeoutPreemptively(Duration.ofMillis(400), () -> {
            new Study(10);
            Thread.sleep(300);
        });
        // TODO ThreadLocal 문제
        // Spring에서 트랜잭션 설정을 가지고 있는 쓰레드와 별개의 쓰레드로 실행하기 때문에
        // 주의해서 사용해야함
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11 })
    //@EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    void create_test_with_condition() {
        //given
        final String test_env = System.getenv("TEST_ENV");
        System.out.println("test_env = " + test_env);
        //when
        //then
/*
        assumeTrue("LOCAL".equalsIgnoreCase(test_env));
        assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> {
            System.out.println("local");
            final Study actual = new Study(10);
            assertThat(actual.getLimit()).isGreaterThan(0);
        });

        assumingThat("gump".equalsIgnoreCase(test_env), () -> {
            System.out.println("gump");
            final Study actual = new Study(10);
            assertThat(actual.getLimit()).isGreaterThan(0);
        });
*/
    }

    @Test
    @DisabledOnOs(OS.MAC)
    void create_test_with_condition_disable() {
        //given
        final String test_env = System.getenv("TEST_ENV");
        System.out.println("test_env = " + test_env);
        //when
    }
}