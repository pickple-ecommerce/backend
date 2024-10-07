package com.pickple.user.domain.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.pickple.user.domain.model.User;
import com.pickple.user.presentation.request.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> searchUsers(UserSearchDto searchDto, Pageable pageable);
}
