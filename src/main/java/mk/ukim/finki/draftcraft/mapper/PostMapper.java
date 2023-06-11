package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.shop.Post;
import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdatePostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "creator", source = "user")
    @Mapping(target = "name", source = "createPostDto.name")
    @Mapping(target = "description", source = "createPostDto.description")
    @Mapping(target = "id", ignore = true)
    Post createDtoToEntity(CreatePostDto createPostDto, User user);

    PostDto toDto(Post post);

    Post updateDtoToEntity(@MappingTarget Post post, UpdatePostDto updatePostDto);

    List<PostDto> listToDto(List<Post> posts);

//    @Named("mapPost")
//    default Post map(CreatePostDto createPostDto) {
//        return Post.builder()
//                .name(createPostDto.getName())
//                ..build();
//    }
}
