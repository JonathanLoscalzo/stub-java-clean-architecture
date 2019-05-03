package com.atlantis.supermarket.core.user.searcher;

import com.atlantis.supermarket.core.user.User;

public interface FindUserByUsername {
    User findByUsername(String username);
}
