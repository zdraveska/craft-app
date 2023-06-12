package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.shop.Post;
import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdatePostDto;
import org.apache.commons.lang3.Range;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface PostMapper {
    @Mapping(target = "creator", source = "user")
    @Mapping(target = "name", source = "createPostDto.name")
    @Mapping(target = "priceRange", source = "createPostDto", qualifiedByName = "mapPriceRange")
    @Mapping(target = "id", ignore = true)
    Post createDtoToEntity(CreatePostDto createPostDto, User user);

//    @Mapping(target = "name", source = "post.name")
    PostDto toDto(Post post);

    Post updateDtoToEntity(@MappingTarget Post post, UpdatePostDto updatePostDto);

    List<PostDto> listToDto(List<Post> posts);

    @Named("mapPriceRange")
    default Range<Integer> map(CreatePostDto createPostDto) {
        return Range.between(createPostDto.getPriceRangeMin(), createPostDto.getPriceRangeMax());
    }
}
