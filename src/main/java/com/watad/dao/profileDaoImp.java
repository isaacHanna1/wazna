package com.watad.dao;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Church;
import com.watad.entity.Profile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class profileDaoImp implements ProfileDao{

    private final EntityManager entityManager;

    public profileDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Profile> findAll() {
        TypedQuery<Profile> query = entityManager.createQuery(
                "SELECT p FROM Profile p JOIN FETCH p.user", Profile.class
        );
        return query.getResultList();
    }

    @Override
    @Transactional
    public void saveProfile(Profile profile) {

        if (profile !=null){
            entityManager.persist(profile);
        }
    }

    @Override
    public Profile getProfileById(int id) {
        try {
            return entityManager.find(Profile.class,id);
        } catch (NoResultException e) {
            throw new NoResultException("There Is no Profile Founded with Id "+id);
        }
    }

    @Override
    @Transactional
    public void editprofile(Profile profile) {
        entityManager.merge(profile);
    }

    @Override
    public String getPrfoileImageName(int id) {
        String jpql = "SELECT p.profileImagePath FROM Profile p WHERE p.id = :id";
        return entityManager.createQuery(jpql, String.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<ProfileDtlDto> findByUserPhone(String phone , int churchId , int meetingId) {
        String  jpql = " select new com.watad.dto.ProfileDtlDto(p.id, p.firstName, p.lastName , p.meetings.id , p.church.id  , p.user.id , p.phone , p.profileImagePath)  from Profile " +
                        " p where p.meetings.id = :meetingId and p.church.id = :churchId and  p.phone like :phone ";
         List<ProfileDtlDto>  result     =
                                        entityManager.createQuery(jpql,ProfileDtlDto.class)
                                            .setParameter("meetingId",meetingId)
                                            .setParameter("churchId",meetingId)
                                                .setParameter("phone", "%" + phone + "%").getResultList();
         if(result.isEmpty()) return new ArrayList<>();
         return  result;
    }
}
