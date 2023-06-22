package mk.ukim.finki.draftcraft.service;


import mk.ukim.finki.draftcraft.domain.model.shop.Post;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdatePostDto;

import java.util.List;
import java.util.Optional;

public interface PostService {

  List<PostDto> getPosts();

  Optional<Post> findById(Long id);

  PostDto createPost(CreatePostDto createPostDto, User user);

  void deletePost(Long id);

  PostDto updatePost(UpdatePostDto updatePostDto, Long postId);
}