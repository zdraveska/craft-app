package mk.ukim.finki.draftcraft.impl;

import mk.ukim.finki.draftcraft.domain.exceptions.PostNotFoundException;
import mk.ukim.finki.draftcraft.domain.model.shop.Post;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.mapper.PostMapper;
import mk.ukim.finki.draftcraft.repository.PostRepository;
import mk.ukim.finki.draftcraft.service.PostService;
import mk.ukim.finki.draftcraft.service.impl.PostServiceImpl;
import mk.ukim.finki.draftcraft.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest extends BaseServiceTest {

  @Mock
  PostRepository postRepository;

  PostMapper postMapper = Mappers.getMapper(PostMapper.class);

  PostService postService;

  @BeforeEach
  public void setup() {
    postService = new PostServiceImpl(postRepository, postMapper);
  }

  @Test
  @WithMockUser(username = USER_EMAIL)
  public void shouldCreatePost() {
    //given
    User user = generateRandomUser(true);
    user.setEmail(USER_EMAIL);
    CreatePostDto createPostDto = getCreatePostDto();
    Post post = postMapper.createDtoToEntity(createPostDto, user);

    //when
    when(postRepository.save(any(Post.class))).thenReturn(post);

    PostDto actual = postService.createPost(createPostDto, user);

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(post.getId());
    assertThat(actual.getName()).isEqualTo(post.getName());
    assertThat(actual.getProductCategory()).isEqualTo(post.getProductCategory());
    assertThat(actual.getShopCategory()).isEqualTo(post.getShopCategory());
    assertThat(actual.getDescription()).isEqualTo(post.getDescription());
    assertThat(actual.getTags()).isEqualTo(post.getTags());
    assertThat(actual.getPriceRange()).isEqualTo(post.getPriceRange());
    assertThat(actual.getCreator().getId()).isEqualTo(user.getId());
    assertThat(actual.getDate()).isEqualTo(post.getDate());
  }

  @Test
  public void shouldThrowPostNotFoundException() {
    //given
    var user = generateRandomUser(true);
    var post = generateRandomPost(true, user);

    //when
    when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

    //then
    Exception exception = assertThrows(PostNotFoundException.class,
        () -> postService.deletePost(post.getId()));
    String expectedMessage = String.format("Post with id: %d not found", post.getId());
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void shouldDeletePost() {
    //given
    var user = generateRandomUser(true);
    Post post = generateRandomPost(true, user);

    //when
    when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
    doNothing().when(postRepository).delete(post);

    postService.deletePost(post.getId());

    //then
    verify(postRepository, times(1)).delete(post);
  }

  @Test
  public void shouldUpdatePost() {
    //given
    var user = generateRandomUser(true);
    var updatePostDto = getUpdatePostDto();
    var post = generateRandomPost(true, user);
    var expectedPost = postMapper.updateDtoToEntity(post, updatePostDto);

    //when
    when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
    when(postRepository.save(any(Post.class))).thenReturn(post);

    PostDto actual = postService.updatePost(updatePostDto, post.getId());

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(expectedPost.getId());
    assertThat(actual.getName()).isEqualTo(expectedPost.getName());
    assertThat(actual.getProductCategory()).isEqualTo(expectedPost.getProductCategory());
    assertThat(actual.getShopCategory()).isEqualTo(expectedPost.getShopCategory());
    assertThat(actual.getDescription()).isEqualTo(expectedPost.getDescription());
    assertThat(actual.getTags()).isEqualTo(expectedPost.getTags());
  }

}
