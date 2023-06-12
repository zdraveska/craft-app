package mk.ukim.finki.draftcraft.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.exceptions.PostNotFoundException;
import mk.ukim.finki.draftcraft.domain.exceptions.UserNotFoundException;
import mk.ukim.finki.draftcraft.domain.shop.Post;
import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdatePostDto;
import mk.ukim.finki.draftcraft.mapper.PostMapper;
import mk.ukim.finki.draftcraft.repository.PostRepository;
import mk.ukim.finki.draftcraft.service.PostService;
import mk.ukim.finki.draftcraft.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static mk.ukim.finki.draftcraft.util.PermissionUtil.getUsername;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    @Override
    public List<PostDto> getPosts() {
        return postMapper.listToDto(postRepository.findAll());
    }

    @Override
    public PostDto createPost(CreatePostDto createPostDto) {
        String username = getUsername();
        User user = userService.findByEmail(username).orElseThrow(()->new UserNotFoundException(username));

        Post post = postMapper.createDtoToEntity(createPostDto, user);
        post.setDate(LocalDate.now());
        postRepository.save(post);
        log.info("Post {} created", post);
        return postMapper.toDto(post);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        postRepository.delete(post);
        log.info("Post has been deleted");
    }

    @Override
    public PostDto updatePost(UpdatePostDto updatePostDto, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        post = postMapper.updateDtoToEntity(post, updatePostDto);
        postRepository.save(post);
        log.info("Post {} modified", post);
        return postMapper.toDto(post);
    }

}