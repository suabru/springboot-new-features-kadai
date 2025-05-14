package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;


@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
//	お気に入り登録・追加
	@Transactional
	public void subscribe(House house, User user) {
		
		Favorite favorite = new Favorite();
		
		favorite.setHouse(house);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
	
	}

	
	public boolean favoriteJudge(House house, User user) {
		Favorite favorite = favoriteRepository.findByHouseAndUser(house, user);
		return favorite != null;
	}
}


    