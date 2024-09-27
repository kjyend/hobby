package com.hobbyproject.service;

import com.hobbyproject.dto.comment.request.CreatedComment;
import com.hobbyproject.dto.comment.response.CommentResponseDto;
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

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void commentCreate(CreatedComment createdComment, Long postId, String loginId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Member writer = memberRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
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
        if(parent != null && parent.getIsDeleted() == DeleteStatus.YES)
            return getDeletableAncestorComment(parent);
        return comment;
    }

    @Override
    public List<CommentResponseDto> getList(Long postId) {
        return commentRepository.getComment(postId);
    }

    @Override
    public boolean isCommentOwner(Long commentId, String loginId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        return comment.getMember().getLoginId().equals(loginId);
    }
}
