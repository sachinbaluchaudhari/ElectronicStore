package com.electonic.store.helper;

import com.electonic.store.dtos.UserDto;
import com.electonic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static <V,U>PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type)
    {
        List<U>  entities= page.getContent();
        List<V> dtoList = entities.stream().map(user -> new ModelMapper().map(user,type)).collect(Collectors.toList());
        PageableResponse response=new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        response.setTotalElements(page.getTotalElements());
        return response;

    }
}
