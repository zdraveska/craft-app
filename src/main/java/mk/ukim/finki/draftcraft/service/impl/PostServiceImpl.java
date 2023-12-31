package mk.ukim.finki.draftcraft.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.exceptions.PostNotFoundException;
import mk.ukim.finki.draftcraft.domain.model.shop.Post;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdatePostDto;
import mk.ukim.finki.draftcraft.mapper.PostMapper;
import mk.ukim.finki.draftcraft.repository.PostRepository;
import mk.ukim.finki.draftcraft.service.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostDto> getPosts() {
        return postMapper.listToDto(postRepository.findAll());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public PostDto createPost(CreatePostDto createPostDto, User user) {
        Post post = postMapper.createDtoToEntity(createPostDto, user);
        post.setDate(LocalDate.now());
        post = postRepository.save(post);
        log.info("Post {} created", post);
        return postMapper.toDto(post);
    }

    @Override
    public void deletePost(Long id) {
        Post post = findById(id).orElseThrow(() -> new PostNotFoundException(id));

        postRepository.delete(post);
        log.info("Post has been deleted");
    }

    @Override
    public PostDto updatePost(UpdatePostDto updatePostDto, Long postId) {
        Post post = findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        post = postMapper.updateDtoToEntity(post, updatePostDto);
        postRepository.save(post);
        log.info("Post {} modified", post);
        return postMapper.toDto(post);
    }

}