package com.hobbyproject.service;

import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.entity.Comment;
import com.hobbyproject.entity.DeleteStatus;
import com.hobbyproject.entity.Member;
import com.hobbyproject.entity.Post;
import com.hobbyproject.repository.CommentRepository;
import com.hobbyproject.repository.MemberRepository;
import com.hobbyproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void commentCreate(CreatedComment createdComment, Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Member writer = memberRepository.findById(member.getMemberId()).orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
        Comment parent=null;
        if(createdComment.getParentId()!=null){
            parent=commentRepository.findById(createdComment.getParentId()).orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        Comment comment = Comment.builder()
                .content(createdComment.getContent())
                .parent(parent)
                .member(writer)
                .post(post)
                .build();

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if(!comment.getReplies().isEmpty()) {
            comment.changeDeletedStatus(DeleteStatus.YES);
        }else{
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }
    private Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParent();
        if(parent != null && parent.getReplies().size() == 1 && parent.getIsDeleted() == DeleteStatus.YES)
            return getDeletableAncestorComment(parent);
        return comment;
    }
}
