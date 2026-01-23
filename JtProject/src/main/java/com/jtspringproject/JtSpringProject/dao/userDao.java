package com.jtspringproject.JtSpringProject.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.sound.midi.Soundbank;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jtspringproject.JtSpringProject.models.User;


@Repository
public class userDao {
	@Autowired
    private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }
   @Transactional
    public List<User> getAllUser() {
        Session session = this.sessionFactory.getCurrentSession();
		List<User>  userList = session.createQuery("from CUSTOMER").list();
        return userList;
    }
    
    @Transactional
	public User saveUser(User user) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(user);
		System.out.println("User added" + user.getId());
        return user;
	}
    
//    public User checkLogin() {
//    	this.sessionFactory.getCurrentSession().
//    }
    @Transactional
    public User getUser(String username,String password) {
    	Query query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where username = :username");
    	query.setParameter("username",username);
    	
    	try {
			User user = (User) query.getSingleResult();
			System.out.println(user.getPassword());
			if(password.equals(user.getPassword())) {
				return user;
			}else {		
				return new User();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			User user = new User();
			return user;
		}
    	
    }

	@Transactional
	public boolean userExists(String username) {
		Query query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where username = :username");
		query.setParameter("username",username);
		return !query.getResultList().isEmpty();
	}

	@Transactional
	public User getUserByUsername(String username) {
	        Query<User> query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where username = :username AND isActive = 1", User.class);
	        query.setParameter("username", username);
	        
	        try {
	            return query.getSingleResult();
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	            return null; 
	        }
    	}

	/* Set isActive to False which is meant triggers soft delete*/
	@Transactional
	public User deleteUser(int userID){
		TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where id = :userID", User.class);
		query.setParameter("userID", userID);
		User user = query.getSingleResult();
		user.setIsActive(false);
		this.sessionFactory.getCurrentSession().save(user);
		return user;
	}

	/* Set isActive to True when User wants to access the website again after deactivating the account*/
	@Transactional
	public User activateUser(int userID){
		TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where id = :userID", User.class);
		query.setParameter("userID", userID);
		User user = query.getSingleResult();
		user.setIsActive(true);
		this.sessionFactory.getCurrentSession().save(user);
		return user;
	}

}