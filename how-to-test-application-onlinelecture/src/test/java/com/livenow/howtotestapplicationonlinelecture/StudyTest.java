package com.livenow.howtotestapplicationonlinelecture;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

/**
 * - 테스트 클래스당 인스턴스를 하나만 만들어 사용한다.
 * - 경우에 따라, 테스트 간에 공유하는 모든 상태를 @BeforeEach 또는 @AfterEach에서 초기화 할 필요가 있다.
 * - @BeforeAll과 @AfterAll을 인스턴스 메소드 또는 인터페이스에 정의한 default 메소드로 정의할 수도 있다.
 *
 * @BeforeAll과 @AfterAll이 static을 붙이지 않아도 됨
 */
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)

/**
 * 선언적 방법
 */
//@ExtendWith(FindSlowTestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {

    /**
     * JUnit은 테스트 메소드 마다 테스트 인스턴스를 새로 만든다.
     - 이것이 기본 전략.
     - 테스트 메소드를 독립적으로 실행하여 예상치 못한 부작용을 방지하기 위함이다.
     - 이 전략을 JUnit 5에서 변경할 수 있다.
     * value 는 항상 1임
     */
    int value = 1;
    private Logger logger = LoggerFactory.getLogger(StudyTest.class);

    /**
     * 프로그래밍 등록
     */
    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = new FindSlowTestExtension(1000L);

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

    @Order(1)
    @Test
    @DisplayName("스터디 만들기")
    void create_new_study() throws InterruptedException {
        //given
        Thread.sleep(1000);

        final Study study = new Study(value++);
        //when
        //then
        assertNotNull(study);
        assertThat(study).isNotNull();
    }

    @Test
    @Disabled
    void create_new_study_again() {
        //given
        System.out.println("logger = " + logger);
        final Study study = new Study(10);
        //when
        //then
        assertNotNull(study);
        assertThat(study).isNotNull();
    }

    @Order(2)
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

    @Order(3)
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
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11})
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

    @Test
    @DisplayName("스터디 만들기 fast")
    @FastTest
    void create_new_study_tagging() {
        //given
        logger.info("fast Test");
        final Study actual = new Study(10);
        //when
        //then
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    @Test
    @DisplayName("스터디 만들기 slow")
    @Tag("slow")
    void create_new_study_tagging2() {
        //given
        final Study actual = new Study(10);
        //when
        //then
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    @DisplayName("반복적 스터디 만들기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
        //DisplayName이 동작하지 않음. name 지정을 안해도 나름 가독성 있음
    void repeatTest(RepetitionInfo repetitionInfo) {
        logger.info("test " + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }


    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"추워", "지는", "날씨"})
    @NullAndEmptySource
    void parameterizedTest(String message) {
        logger.info(message);
    }

    /**
     * 인자를 하나만 받을떄
     */
    @DisplayName("컨버터와 함꼐 스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTestWithSimpleArgumentConverter(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimit());
    }

    @DisplayName("컨버터와 함꼐 스터디 만들기 - Csv")
    @ParameterizedTest(name = "{index} {displayName} message={0}, {1}")
    @CsvSource(value = {"10, '자바 스터디'", "20, '스프링'"}, delimiter = ',')
    void parameterizedTestWithCvs(Integer limit, String name) {
        System.out.println(new Study(limit, name));
    }

    @DisplayName("컨버터와 함꼐 스터디 만들기 - Csv with Accessor")
    @ParameterizedTest(name = "{index} {displayName} message={0} {1}")
    @CsvSource(value = {"10, '자바 스터디'", "20, '스프링'"}, delimiter = ',')
    void parameterizedTestWithCvsAccessor(ArgumentsAccessor argumentsAccessor) {
        System.out.println(new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1)));
    }

    @DisplayName("컨버터와 함꼐 스터디 만들기 - Csv with Aggregator")
    @ParameterizedTest(name = "{index} {displayName} message={0}, {1}")
    @CsvSource(value = {"10, '자바 스터디'", "20, '스프링'"}, delimiter = ',')
    void parameterizedTestWithCvsAggregator(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }

    /**
     * 인자를 하나만 받을떄
     */
    static class StudyConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertThat(targetType).isEqualTo(Study.class);
            assertEquals(Study.class, targetType, "Can only conver to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }

    /**
     * 인자를 여러개 받을때
     * static inner class이거나, public class일경우만 사용
     */
    static class StudyAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return new Study(accessor.getInteger(0), accessor.getString(1));
        }
    }
}

