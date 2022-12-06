package sparta.startertask.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sparta.startertask.dto.PostReq;
import sparta.startertask.dto.PostRes;
import sparta.startertask.service.PostService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostRes> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{post-id}")
    public PostRes getOnePost(@PathVariable("post-id") Long postId) {
        return postService.showOnePost(postId);
    }

    @PostMapping
    public PostRes uploadPost(@RequestBody PostReq postReq) {
        return postService.uploadPost(postReq);
    }

    @PutMapping("/{post_id}")
    public PostRes editPost(@PathVariable("post_id") Long postId, @RequestBody PostReq editPostReq) {
        return postService.editPost(postId, editPostReq);
    }

    @DeleteMapping("/{post_id}")
    public Map<String, Boolean> deletePost(@PathVariable("post_id") Long postId, @RequestBody Map<String, String> password) {
        return postService.deletePost(postId, password);
    }

    public void deleteAll() {
        postService.deleteAll();
    }
}

// 1 , {"password" : "password"}// 시점, 권한의 문제
// 1 , password.get("password")
// dto로  delete 할때 인가를 체크할지.. 인가를 먼저 하고 삭제 ?  - 인가를 어디서 하느냐..
// // 응답에 대해서도 체크하는 test code - http status
// 10가지 넘는거에 대해 다 테스트 해야하는가 ? edge 케이스만 ? - 예상가능한 부분 다 테스트 하도록 - 기본적으론 명세서
// 잘못된 인풋 -에러코드 잘 응답 ? HTTP status
// 기본적으로 테스트코드는 명세해준 내용들 기반 -  - 기능 정의서에 명시된 인풋과 아웃풋
// 단위 테스트코드 -컨트롤러의 기능만// 통합테스트 - 컨트롤러 뒤쪽의 모든 부분
// 인수 테스트? - 운영 측면 ....
// 기능 테스트 -
// 단위테스트 - 함수단위, 클래스단위 .. <(넓은범위) < 기능테스트(용어 잘안씀..) < 통합테스트 - 외부환경까지 고려한 큰 개념 테스트

// 서비스에 대해서도 테스트코드를 작성해야하는가 ? - 서비스 함수는 다 짠다 -
// 컨트롤러에서 호출되기 시작하지만, 서비스에서 짤 수 있는 코드 매우 많다 - 서비스 함수 관점에서 테스트할 수 있는게 더 있다
// 서비스에서 매개변수 넘겨받는 값 - 컨트롤러에서 자체 처리해서 서비스로 넘겨주는 값이 있을 수 있다
// 서비스 내부에서 함수가 잘 돌아가는지 체크할 수 있어야한다.
// 컨트롤러와 독립적으로 서비스가 잘 돌아가는지 테스트하는 용도

// 엔티티 주석이 필요할정도로 로직을 좀 넣었고, 예외가 발생할 가능성이 있으면 테스트코드 필요
// 너무 기본적인건 x
//

// 작성하라면은, entity, dto 등등에 대해서도 테스트코드 작성이 가능한데, 얘도 ?
// 테스트 서버를 띄우는 MockMvc(단위테스트- api 호출)  ... 어떻게 쓰는걸까? - controller <=> mockMvc
// 테스트 코드 작성 기준...

// IllegalArgumentException 은 굳이 안잡아줘도 되나요 ? - 에러 응답은 날리는 상태
// 에러가 날라가는건 쓰레드 단위.. - 해당 쓰레드는 종료된 상태

// 커스텀 예외를 만들텐데, 이것도 배워야한다. 뭐를 상속받고,
// 예외 테스트때 메세지도 테스트 해야되나요 ? // 호불호
// 비즈니스 로직 - 필요한 예외들이 따로 있다 - 에러메세지에 대한 이해가 가능하니까..

// 코드 수정을
// test 코드 - 중심, 왜 짜야되는지 짰을때 장점이 뭔지 ..
// test 단위 별로 왜 단위를 나누는지 - 장점은 뭔지 - component integrate
// edge case 찾아내기
// end to end TEST - 사용자 end 부터 DB or 실제 결제 되는 서버 end 까지
// 변경 가능성을 항상 신경써야한다 -
// 나의 얕은 기억력을 대신해줄 중요한역할 .
