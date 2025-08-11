package com.watad.dao;

import com.watad.dto.RoleDto;
import com.watad.dto.UserCountsDto;
import com.watad.entity.Role;
import com.watad.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class UserDaoImp implements  UserDao{



    private final EntityManager entityManager;


    public UserDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveUser(User user) {
        entityManager.merge(user);
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

        TypedQuery<User> theQuery = entityManager.createQuery(" FROM User u JOIN FETCH u.roles where userName =: data",User.class);
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

    @Override
    public UserCountsDto getCountsInMeeting(int churchId, int meetingId) {
        String jpql  = """
                            SELECT new com.watad.dto.UserCountsDto(
                                COUNT(u.id),
                                SUM(CASE WHEN u.isEnabled = true THEN 1 ELSE 0 END),
                                SUM(CASE WHEN u.isEnabled = false THEN 1 ELSE 0 END)
                            )
                            FROM User u
                            JOIN u.profile p
                            WHERE p.meetings.id = :meetingId
                            AND p.church.id = :churchId
                      """;

                        TypedQuery<UserCountsDto> query = entityManager.createQuery(jpql, UserCountsDto.class);
                        query.setParameter("meetingId", meetingId);
                        query.setParameter("churchId", churchId);
                        return query.getSingleResult();
    }

    @Override
    public void activeOrDisactiveUser(User user) {
            entityManager.merge(user);
            System.out.println("the status of user is "+user.isEnabled());
            entityManager.flush();

    }

    @Override
    public List<RoleDto> getUserRole(String userName) {
        TypedQuery<RoleDto> query = entityManager.createQuery("SELECT NEW  " +
                " com.watad.dto.RoleDto(r.id , r.roleName) FROM  User u  Join u.roles r " +
                " where u.userName =:userName", RoleDto.class);
        query.setParameter("userName",userName);
        return query.getResultList();
    }

    @Override
    public void updateUserRole(User user, Role role) {
        if (user == null || role == null) {
            throw new IllegalArgumentException("User and Role must not be null");
        }
        System.out.println("the user  "+user.getUserName());
        System.out.println("the role  "+role.getRoleName());
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role);
        user.setRoles(newRoles);
        entityManager.merge(user);
    }


}
