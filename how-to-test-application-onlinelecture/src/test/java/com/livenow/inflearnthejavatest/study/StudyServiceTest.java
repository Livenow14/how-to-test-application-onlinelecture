package com.livenow.inflearnthejavatest.study;

import com.livenow.inflearnthejavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

/*
    @Mock
    private MemberService memberService;

    @Mock
    private StudyRepository studyRepository;
*/

    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        //when
        //then
    }
}