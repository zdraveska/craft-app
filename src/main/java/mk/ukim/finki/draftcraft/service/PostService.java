package mk.ukim.finki.draftcraft.service;


import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdatePostDto;

import java.util.List;

public interface PostService {

  List<PostDto> getPosts();

  PostDto createPost(CreatePostDto createPostDto);

  void deletePost(Long id);

  PostDto updatePost(UpdatePostDto updatePostDto, Long postId);
}