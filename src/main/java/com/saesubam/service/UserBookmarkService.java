package com.saesubam.service;

import java.util.List;
import com.saesubam.model.Profiles;
import com.saesubam.model.UserBookmark;
import com.saesubam.model.Users;

public interface UserBookmarkService {

    boolean toggleBookmark(Users user, Profiles profile);

    List<UserBookmark> getUserBookmarks(Users user);

    boolean isBookmarked(Users user, Profiles profile);
}
