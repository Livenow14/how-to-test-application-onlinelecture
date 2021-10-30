package com.livenow.inflearnthejavatest.study;

import com.livenow.inflearnthejavatest.domain.Member;
import com.livenow.inflearnthejavatest.domain.Study;
import com.livenow.inflearnthejavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

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

    @Test
    void createStudyService2(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        final Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");

        final Study study = new Study(10, "java");
        //when
        //Mockito.when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(memberService.findById(anyLong()))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

        final Optional<Member> findById = memberService.findById(1L);
        //then
        assertThat("keesun@email.com").isEqualTo(findById.get().getEmail());
        assertThatThrownBy(() -> memberService.findById(1L))
                .isInstanceOf(RuntimeException.class);
        assertThat(memberService.findById(1L)).isEqualTo(Optional.empty());
        assertThatThrownBy(() -> memberService.validate(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createStudyService3(@Mock MemberService memberService,
                             @Mock StudyRepository studyRepository) {
        //given
        final Study study = new Study(10, "테스트");
        study.setOwnerId(1L);
        final Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");
        //when
        willReturn(Optional.of(member)).given(memberService).findById(1L);
        willReturn(study).given(studyRepository).save(study);
        final Member foundMember = memberService.findById(1L).get();
        final Study savedStudy = studyRepository.save(study);
        //then
        assertThat(savedStudy.getOwnerId()).isNotNull();
        assertThat(foundMember.getId()).isEqualTo(savedStudy.getOwnerId());
    }

    @Test
    void createStudyService4(@Mock MemberService memberService,
                             @Mock StudyRepository studyRepository) {
        //given
        final StudyService studyService = new StudyService(memberService, studyRepository);
        final Study study = new Study(10, "테스트");
        study.setOwnerId(1L);
        final Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");
        //when
        willReturn(Optional.of(member)).given(memberService).findById(1L);
        willReturn(study).given(studyRepository).save(study);
        studyService.createNewStudy(1L, study);
        //then
        verify(memberService, times(1)).notify(study);
        verify(memberService, times(1)).notify(member);

        /**
         * 순서를 보장받고 싶을 때
         */
        final InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(member);

        /**
         * 더이상 추가적인 액션을 원하지 않는다면
         */
        //verifyNoInteractions(memberService);
    }
}