package com.bit.web.web;

import com.bit.web.entity.Member;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

//사용자의 URL 요청을 처리하겠다.
@Controller
// @RestControlle가 붙으면 무조건 return이 json
//공통적으로 아용되는 URL이라면 RequestMapping을 통해 묶어버릴 수 있다.
// 즉 자동으로 Url을 /board/~~형태로 만들어주게 된다는 의미.
@RequestMapping("/board")
public class BoardController {
    // Logger 컨트롤러가 동작하는 log들을 볼수 있다.
    // static 이 붙어있으면 메모리에 상주한다 쉽게 생각하면 메모리에 통채로 박혀있다고생각하면된다.. ex) main 보면 new 가 안붙어있네? static때문에
    // 결국 data 영역에 상시 배치되어 있는 것이다.

    // static 을 남발하는것은 자제해야 한다. 이유는 메모리에 상주가게 되어 그만큼 메모리를 소비하므로
    // 대규모 시스템이 동작할때 static의 남발이 성능저하를 유발한다.

    // new 라는 것은 Healp 에 할당하는 것이고 Heap에 할당한다는 것은 동적으로 할당을 한다는 것이므로
    // 실제 Run Time 에 실행하면서 할당을 하게되어 속도가 느리다.
    // 그러나 static으로 박아놓은 것은 메모리는 낭비하지만 프로그램이 동작할떄 초기부터 세팅되어 있는 것이라
    // 별도의 동적할당 메커니즘이 없어어 속도측면에서 이점을 얻을 수 있다.
    static Logger log = LoggerFactory.getLogger(BoardController.class);
//    private BoardServiceImpl service;
//
//    public BoardController(BoardServiceImpl service){
//        this.service= service;
//    }

    //사용자의 URL 요청중 /test로 들어오는 녀석이 있다면 나한테보내줘.
    @GetMapping("/test")  // url 에 대한 get 요청이 들어왔을때 동작이 하는 것. 컨트롤러가 받아서 test야? 그러면 동작
    public String test(Model model){
            // HTML 태그중 템플릿으로 받을 수 있는 태그가 있는데 해당 태그에서 message를 받을 수 있게 해주는 작업
            // 즉 여기서 message는 일종의 key 값이라고 생각하면 되고
            // 그 뒤쪽에 있는 내용이 실제 value라고 생각하면 되겠다.
            model.addAttribute("message", "Hello Spring-Vue-MySQL-webGL");
      // 이것은 Thymeleaf에 의해서 자동으로 resources/templates/test.html 로 정보를 출력하겠다는 의미가 된다.
        return "test";
    }
    @GetMapping("/")
    public String home() {
        log.info("home");
        return "index";
    }
    @GetMapping("/imgServer")
    public ResponseEntity<byte[]> imgServe() throws Exception {
        log.info("imgServe");

        //stream이란 무엇일까? 제일 중요한 것은 순서다.
//        게임에서 우리가 w qr 를 눌렀는데 qwr이 나간다면?
//        절대로 이러한연산에 대해서 순서가 뒤바뀌어서는 안된다. 이것을 보장해주는 것이 Stream(순서보장)이다
        InputStream in = null;
        //현재 해당 컨트롤러에서 처리하려는 정보가 이미지 파일이다.
        // 이미지 파일은 파이썬에서도 봤듰이 픽셀행렬들로 구성되어있다.
//        각각의 값들은 0~255였고 우리가 저부분의 값들을 조절해서 영상처리를 했었다
//        그러므로 엔티티 또한 0~255 를 표현할 수 있는 byte 형태의 배열로 저장하면된다
//        결론: 이미지는 byte[]형태로 처리할 수있다.
        ResponseEntity<byte[]> entity = null;
        try {
//            사용자가 URL을 입력하는 것은 Request이고 서버가 이에 대해 응답하는 response로서
//            이미지를 전달하므로 반드시 Http Response Header에 content-type image/jpg여야한다
            HttpHeaders headers = new HttpHeaders();
            in = new FileInputStream("src/main/resources/static/dog.jpg");
            headers.setContentType(MediaType.IMAGE_JPEG);
            // IOUtils는 Apache commons-io가 필요하여
            // build.gradle에 commons-io를 추가해서 사용해야 한다.
            // toByteArray는 바이트를 array(배열) 형태로 만드는 작업
            // 위에서 만든 header와 상태코드(생성성공)을 Response(응답)에 넣는 작업을 한다.
            // vue의 axios등을 활용할 경우에도 ResponseEntity를 잘이해하고 활용할 수 있으면
            // backend 단과 통신이 굉장히 용이하고 쉬워진다.
            // ResponseEntity 에 대한 요약: 요청에 대한 응답을 어떻게 할 것인가를 다룸
            entity =
                    new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
        } catch(Exception e) {
            //파일 처리를 제대로 하지 못했다면  BAD_REQUEST 를 날림
            e.printStackTrace();
            entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        } finally {
            // stream 특성중 하나 열었으면 반드시 닫아야 한다. 대표적인 메모리 릭현상의 주원인이므로 주의해야함함            in.close();
        }
        // 올바르게 처리한 경우든 아닌 경우든 entity를 리턴해준다.
        // 처리가 제대로 되었는지 안되었는지를 요청ㅎ나 쪽에 알려줄 수 있다.
        return entity;
    }

    @GetMapping("/fileServer")
    public ResponseEntity<byte[]> fileServe() throws Exception {
        log.info("fileServe");
        String fileName = "jupyter_workspace.zip";
        InputStream in = null;
        ResponseEntity<byte[]> entity = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            in = new FileInputStream("src/main/resources/static/" + fileName);
            //Media.Applicaiotn Octet Stream 은 보통 파일을 다운받거나 할때 사용한다.
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // UTF-8이 나온 이유는 파일을 다운로드할 떄 인토킫ㅇ 포맷을 맞춰야하기ㄸ ㅐ문에 utf-8내부에 서 캐릭터 셋 설정이 있는데
            // ISO-8859-1(국제 표준의 문자집합) 을 넣어줘야 파일이 잘 다운로드 된다.
            // i
            headers.add("Content-Disposition",
                    "attachment: filename=\"" +
                            new String(fileName.getBytes("UTF-8"),
                                    "ISO-8859-1") + "\"");
            entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
        } catch(Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        } finally {
            in.close();
        }
        return entity;
    }
       // @Restcontroller 에 의해 리턴하는 것이 json화 되어 넘어간다.
    // AWesome Json Viewer를 통해 살펴보면 실제 여기서 처리디는 데이터 값들을 눈으로 확인할 수 있다.\
//    우리가 만든 Member entitiy가 객체화되어 jjson으로 넘어가는것을 볼 수 있따.
    @RequestMapping(value = "/member", method = RequestMethod.GET)
    public Member retJSONMember(){
        log.info("retJSONMember");
        Member member = new Member();
      return member;
    }

    // List인 경우에는 데이터가 배열처럼 들어오게 되고 나머지는 위의 경우와 동일하다.
    @GetMapping("/memberList")
    public List<Member> retListJSONMember(){
        log.info("retListJSONMember");
        List<Member> list = new ArrayList<Member>();

        Member mem1 = new Member();
        list.add(mem1);
        Member mem2 = new Member();
        list.add(mem2);
        return list;
    }
    //Map을 기반으로 하기 때문에 단순히 키값에서 특정한 값으로 접근한다기 보다는
//    value값으로 지정된 녀석이 동일한 hash인지 list인지 파악하는 것이 중요하다.
//    넘어온 정보가 리스트라면 바로 접근이 가능할 것이고
//    해쉬라면 key값을 한번 더 터야할 것이다.
    @GetMapping("/memberMap")
    public Map<String,Member> retMapJSONMember(){
        log.info("retMapJSONMember");

        Map<String, Member> map = new HashMap<String, Member>();
        Member mem1 = new Member();
        map.put("key1",mem1);
        Member mem2 = new Member();
        map.put("key2",mem2);
        return map;
    }
//    ResponseEntity는 우리가 처리한 Entitiy를 가지고 현재 http의 상태에 대한 응답을 처리할 수 있도록 ㅎ도와주는 클래스
//    ResponseEntity<엔티티> 이와 같은 형식으로 많이 사용함
//            DB의 경우 ; 서비스 -> 저장소 -> ResponseEntity<엔티티> 배치할 entity를 가져옴;
//            엔티티: 데이터
//    Entity: 데이터(어떤 경우에도 오염되어서는 안된다.)
//    이 데이터를 가공하가 위한 기타 여러가지 것들에 의해 Entity가 변경되어서는 안된다는 의미
    // generic을 요악하자면 데이터 타입에 종속적이지 않게 만들겠다는 뜻
    @GetMapping("/responseTest")
    public ResponseEntity<Void> responseTest(){
        log.info("Response test");
//        HttpStatus.OK <----성공적인 응답 200번
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @GetMapping("/register")
    public String registerGet() {
        log.info("registerGet");
        return "register";
    }
    @PostMapping("/register")
    public String registerPost() {
        log.info("registerPost");
        return "register";
    }
    // 사용자의 URL 요청중 /board/register를 보내면 registerForm() 매서드가 동작
    @GetMapping("/registerGet")
    public String registerGetForm(String userId, String passwd, Date date) {
        log.info("registerGetForm");
        log.info("userId = " + userId + "passwd = " + passwd);
        return "success";
    }
    @PostMapping("/registerPost")
    public String registerPostForm(String userId, String passwd,Date date) {
        log.info("registerPostForm");
        log.info("userId = " + userId + "passwd = " + passwd);
        log.info("Date = " + date);
        return "success";
    }
    @GetMapping("/modify")
    public String modifyGetForm() {
        log.info("modifyGetForm");
        return "modify";
    }
    @PostMapping("/modify")
    public String modifyPostForm() {
        log.info("modifyPostForm");
        return "modify";
    }
    @PostMapping("/remove")
    public String removeForm() {
        log.info("removeForm");
        return "remove";
    }
    @GetMapping("/list")
    public String listForm() {
        log.info("listForm");
        return "list";
    }
    @GetMapping("/read")
    public String readForm() {
        log.info("read");
        return "read";
    }
    // 우리가 특정 게시글을 읽을때
    // 1번째 글, 100번째 글, 1000번째글
    // 각각을 기억하고 URL에 /board/read/100을 할 수 있는 것이 아니다.
    // 그러므로 선택한 제목의 내용을 읽고자 할 때
    // 해당 글에 대한 URL 처리를 아래와 같이 가변적으로 할 필요가 있다.
    // 이런 경우 가변적인 숫자를 처리하기 위한 것이 @PathVariable 이다.
    // 즉 URL {boardNo}로 들어온것을 @PathVariable이 받아서 int boardNo로 준다는 의미
    @RequestMapping(value = "/read/{boardNo}")
    public String readForm(@PathVariable("boardNo") int boardNo) {
        log.info("readForm: " + boardNo);
        return "read";
    }
}