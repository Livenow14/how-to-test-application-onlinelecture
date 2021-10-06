package com.livenow.howtotestapplicationonlinelecture;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void create() {
        //given
        final Study study = new Study();
        //when
        //then
        assertNotNull(study);
        assertThat(study).isNotNull();
    }

    @Test
    @Disabled
    void create1() {
        //given
        final Study study = new Study();
        //when
        //then
        assertNotNull(study);
        assertThat(study).isNotNull();
    }
}