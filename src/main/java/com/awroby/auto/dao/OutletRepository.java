package com.awroby.auto.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.awroby.auto.objects.Outlet;

public interface OutletRepository extends MongoRepository<Outlet, String>{

	public List<Outlet> findAll();
	public Outlet findById(String id);
	
}
