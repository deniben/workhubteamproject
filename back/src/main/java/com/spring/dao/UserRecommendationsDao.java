package com.spring.dao;

import java.util.List;

import com.spring.entity.User;
import com.spring.entity.UserRecommendations;

public interface UserRecommendationsDao extends BaseDao<UserRecommendations> {

	List<UserRecommendations> getUserRecommendations(User user);

}
