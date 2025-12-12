package com.example.board.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.member.entity.Member;
import com.example.board.member.repository.MemberRepository;
import com.example.board.post.entity.Board;
import com.example.board.post.repository.BoardRepository;
import com.example.board.reply.entity.Reply;
import com.example.board.reply.repository.ReplyRepository;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository bR;
    @Autowired
    private MemberRepository mR;
    @Autowired
    private ReplyRepository rR;

    @Test
    @Commit
    public void insertGuestTest() {
        Member m = Member.builder()
                .email("guest")
                .password("guest")
                .name("손님")
                .build();
        mR.save(m);
    }

    @Test
    @Commit
    public void insertMemberTest() {
        List<Member> mList = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Member m = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .password("1111")
                    .name("user" + i)
                    .build();
            mList.add(m);
        });
        mR.saveAll(mList);
    }

    @Test
    @Commit
    public void insertBoardTest() {
        List<Board> bList = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            int rand = (int) (Math.random() * 10) + 1;
            Member randM = mR.findById("user" + rand + "@gmail.com").get();
            Board b = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer(randM)
                    .build();
            bList.add(b);
        });
        bR.saveAll(bList);
    }

    @Test
    @Commit
    public void insertReplyTest() {
        List<Reply> rList = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            int rand = (int) (Math.random() * 10) + 1;
            long randId = (long) (Math.random() * 100) + 1;
            Member randM = mR.findById("user" + rand + "@gmail.com").get();
            Board randB = bR.findById(randId).get();
            Reply b = Reply.builder()
                    .text("reply text..." + i)
                    .replayer(randM.getName())
                    .board(randB)
                    .build();
            rList.add(b);
        });
        rR.saveAll(rList);
    }

    @Test
    @Transactional(readOnly = true)
    public void readBoardTest() {
        var list = bR.findAll();
        list.forEach(b -> {
            System.out.println(b);
            System.out.println(b.getWriter());
        });
    }

    @Test
    public void getBoardWithWriterListTest() {
        var result = bR.getBoardWithWriterList();
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void getBoardWithReplyTest() {
        // var board = bR.findById(30L).get();
        // System.out.println(board);
        // System.out.println(board.getReplies());
        var result = bR.getBoardWithReply(30L);
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void getBoardWithReplyCountTest() {
        var pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        var result = bR.getBoardWithReplyCount(pageable);
        for (Object[] objects : result) {
            var board = (Board) objects[0];
            var member = (Member) objects[1];
            var count = (Long) objects[2];
            System.out.println(board);
            System.out.println(member);
            System.out.println("Reply Count : " + count);
        }
    }

    @Test
    @Transactional(readOnly = true)
    public void getListWithFetchJoinTest() {
        // Service에서 이 부분을 호출
        List<Board> boards = bR.list();

        // 이 시점에 쿼리는 단 1번의 JOIN FETCH로 실행됩니다. (N+1 문제 해결)
        boards.forEach(board -> {
            System.out.println(board);
            System.out.println(board.getWriter()); // 추가 쿼리 없이 writer 정보 접근 가능
        });
    }
}
