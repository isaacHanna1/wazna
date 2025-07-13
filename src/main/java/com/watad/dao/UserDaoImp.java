package com.watad.dao;

import com.watad.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements  UserDao{



    private final EntityManager entityManager;


    public UserDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public Optional<User> findUserBYId(int id) {
        try{
          return Optional.of(entityManager.find(User.class,id));
        }catch (NoResultException ex){
            return  Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUserNameForLogin(String userName) {

        TypedQuery<User> theQuery = entityManager.createQuery(" FROM User where userName =: data",User.class);
                theQuery.setParameter("data",userName);

                try {
                    return Optional.of(theQuery.getSingleResult());
                } catch (NoResultException e) {
                    return Optional.empty();
                }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        try {
            TypedQuery<User> theQuery = entityManager.createQuery("From User u where u.userName =:data ", User.class);
            theQuery.setParameter("data", phone);
            User user = theQuery.getSingleResult();
        }catch (NoResultException ex){
            return false;
        }
        return true;
    }

    @Override
    public List<User> findByRoleId(int role_id) {
        String JPQL = """
                              SELECT u FROM User u
                                JOIN u.roles r
                                JOIN u.profile p
                                WHERE r.id = :roleId
                """;
        List<User> users =  new ArrayList<>();
        users = entityManager.createQuery(JPQL,User.class).setParameter("roleId",role_id).getResultList();
        return  users;

    }
}
